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
import com.tolgaozgun.sprintplanning.data.model.User
import com.tolgaozgun.sprintplanning.repository.LobbyRepository
import com.tolgaozgun.sprintplanning.util.Converters
import com.tolgaozgun.sprintplanning.util.FirebaseUtil
import com.tolgaozgun.sprintplanning.util.LobbyUtil
import com.tolgaozgun.sprintplanning.util.LocalUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
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

    suspend fun leaveLobby(context: Context, lobby: Lobby): Boolean{
        val lobbies: CollectionReference = firestore.collection("lobbies")

        clearLocalUserVote(context)
        return true
    }

    suspend fun clearLocalUserVote(context: Context): Boolean{
        val user: User = LocalUtil.loadLocalUser(context)
        val users: CollectionReference = firestore.collection("users")
        val userRef = users.document(user.id.toString())
        firestore.runBatch {
            userRef.update("vote", -1)
            userRef.update("has_voted", false)
        }.await()
        return true
    }

    suspend fun joinLobby(code: String, context: Context) : Lobby?{
        val lobbies: CollectionReference = firestore.collection("lobbies")
        var lobby: Lobby? = null

        var result = lobbies.get().await()

        if(result != null){
            for(existingLobby in result){
                if(existingLobby.contains("code")){
                    val currentCode: String = existingLobby.getString("code")!!
                    Log.d("JOIN_OBBY", "Checking $currentCode against our $code")

                    if(currentCode == code){
                        lobby = Lobby.loadSnapshot(existingLobby)
                        break
                    }
                }
            }
        }


        if(lobby != null){
            val userList: MutableList<User> = lobby.users.toMutableList()
            val localUser: User = LocalUtil.loadLocalUser(context)

            userList.add(localUser)
            lobby.users = userList.toList()

            val serializedLobby: SerializedLobby = Lobby.serialize(lobby)
            lobbies.document(lobby.code).update("users", serializedLobby.users).await()
            var addedLobby = lobbies.document(lobby.code).get().await()
            if(addedLobby != null){
                return Lobby.loadSnapshot(addedLobby)
            }
        }else{
            Log.d("JOIN_LOBBY", "Lobby not found")
            // TODO: Join failed
            return null
        }
        return null
    }

    suspend fun updateUser(context: Context): Boolean{
        val user: User = LocalUtil.loadLocalUser(context)
        val users: CollectionReference = firestore.collection("users")
        val userRef = users.document(user.id.toString())
        firestore.runBatch{ batch ->
            userRef.update("avatar", user.avatarUrl)
            userRef.update("name", user.name)
        }.await()
        return true
    }

    suspend fun vote(value: Int, userId: String): Boolean{
        val users: CollectionReference = firestore.collection("users")
        val user = users.document(userId)

        firestore.runBatch{ batch ->
            user.update("vote", value)
            user.update("has_voted", true)
        }.await()

        return true
    }

    suspend fun loadUsers(uuidList: List<UUID>): List<User>{
        var list: MutableList<UUID> = uuidList.toMutableList()
        var userList: MutableList<User> = mutableListOf()
        val users = firestore.collection("users").get().await()
        if(users != null){
            for(userRef in users){
                val user: User = Converters.snapshotToUser(userRef)
                if(list.contains(user.id)){
                    userList.add(user)
                }
            }
        }
        return userList.toList()
    }

    suspend fun createLobby(pendingLobby: Lobby) : Lobby{
        val lobbies: CollectionReference = firestore.collection("lobbies")
        val codes: MutableList<String> = mutableListOf()

        val serializedLobby: SerializedLobby = Lobby.serialize(pendingLobby)

        val lobbiesList = lobbies.get().await()

        if(lobbiesList != null) {
            for (existingLobby in lobbiesList) {
                if (existingLobby.contains("code")) {
                    val code: String = existingLobby.getString("code")!!
                    codes.add(code)
                }
            }
        }

        var currentCode: String = serializedLobby.code
        while(codes.contains(currentCode)){
            currentCode = LobbyUtil.createCode()
        }
        serializedLobby.code = currentCode

        //TODO: Add success and error returns
        lobbies.document(serializedLobby.code).set(serializedLobby)
            .addOnSuccessListener { snapshot ->
                Log.d("CREATELOBBY", "Successfully added lobby")
            }
            .addOnFailureListener { exception ->
                Log.d("CREATELOBBY", "Failed to add lobby $exception")
            }.await()
        // TODO: Change the return to serialized version of document reference
        return pendingLobby
    }




}