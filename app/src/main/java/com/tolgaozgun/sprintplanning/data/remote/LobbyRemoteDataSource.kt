package com.tolgaozgun.sprintplanning.data.remote

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.liveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.tolgaozgun.sprintplanning.data.local.LobbyDatabase
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.data.model.SerializedLobby
import com.tolgaozgun.sprintplanning.repository.LobbyRepository
import com.tolgaozgun.sprintplanning.util.FirebaseUtil
import com.tolgaozgun.sprintplanning.util.LobbyUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import java.util.*

class LobbyRemoteDataSource(
    private val firestore: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher,
) {

    companion object{
        @Volatile
        private var INSTANCE : LobbyRemoteDataSource? = null

        fun getInstance(): LobbyRemoteDataSource {
            if(INSTANCE == null){
                synchronized(this){
                    if(INSTANCE == null){
                        INSTANCE = LobbyRemoteDataSource(FirebaseUtil().getFirestore(), Dispatchers.IO)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    suspend fun joinLobby(code: String, context: Context) : Lobby?{
        val lobbies: CollectionReference = firestore.collection("lobbies")
        var lobby: Lobby? = null

        lobbies.get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val existingLobbies = task.result
                if(existingLobbies != null){
                    for(existingLobby in existingLobbies){
                        if(existingLobby.contains("code")){
                            val currentCode: String = existingLobby.getString("code")!!
                            if(currentCode == code){
                                lobby = Lobby.loadSnapshot(existingLobby)
                                break
                            }
                        }
                    }
                }
            }
        }.await()

        if(lobby != null){
            // TODO: Join Success
            val userList: MutableList<UUID> = lobby!!.users.toMutableList()
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("local_user", Context.MODE_PRIVATE)


            val idString: String = sharedPreferences.getString("id", null)!!

            userList.add(UUID.fromString(idString))
            lobby!!.users = userList.toList()

            val serializedLobby: SerializedLobby = Lobby.serialize(lobby!!)
            lobbies.document(lobby!!.code).update("users", serializedLobby.users).await()
        }else{
            // TODO: Join failed
        }
        return lobby
    }

    suspend fun vote(value: Int, userId: String): Boolean{
        var result: Boolean = false
        val users: CollectionReference = firestore.collection("users")
        users.document(userId).update("vote", value).addOnCompleteListener {
            result = true
        }.addOnFailureListener{
            result = false
        }
        return result
    }

    suspend fun createLobby(pendingLobby: Lobby) : Lobby{
        val lobbies: CollectionReference = firestore.collection("lobbies")
        val codes: MutableList<String> = mutableListOf()

        val serializedLobby: SerializedLobby = Lobby.serialize(pendingLobby)

        lobbies.get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val existingLobbies = task.result
                if(existingLobbies != null){
                    for(existingLobby in existingLobbies){
                        if(existingLobby.contains("code")){
                            val code: String = existingLobby.getString("code")!!
                            codes.add(code)
                        }
                    }
                }
            }
        }.await()


        var currentCode: String = serializedLobby.code
        while(codes.contains(currentCode)){
            currentCode = LobbyUtil.createCode()
        }
        serializedLobby.code = currentCode

        //TODO: Add success and error returns
        val documentReference : DocumentReference = lobbies.add(serializedLobby)
            .addOnSuccessListener { snapshot ->
                Log.d("CREATELOBBY", "Successfully added lobby with id-> ${snapshot.id}")
            }
            .addOnFailureListener { exception ->
                Log.d("CREATELOBBY", "Failed to add lobby $exception")
            }.await()
        // TODO: Change the return to serialized version of document reference
        return pendingLobby
    }




}