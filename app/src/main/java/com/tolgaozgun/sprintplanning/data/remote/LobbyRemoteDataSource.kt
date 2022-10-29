package com.tolgaozgun.sprintplanning.data.remote

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.*
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.data.model.SerializedLobby
import com.tolgaozgun.sprintplanning.data.model.User
import com.tolgaozgun.sprintplanning.util.Converters
import com.tolgaozgun.sprintplanning.util.FirebaseUtil
import com.tolgaozgun.sprintplanning.util.LobbyUtil
import com.tolgaozgun.sprintplanning.util.LocalUtil
import com.tolgaozgun.sprintplanning.viewmodels.lobby.LobbyViewModel
import kotlinx.coroutines.*
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
        val lobbiesRef: CollectionReference = firestore.collection("lobbies")
        val lobby = lobbiesRef.document(lobby.code)
        val localUser: User = LocalUtil.loadLocalUser(context)
        lobby.update("users", FieldValue.arrayRemove(localUser.id.toString()))
        clearLocalUserVote(context)
        return true
    }

    suspend fun addLocalUser(context: Context){
        val user: User = LocalUtil.loadLocalUser(context)
        val users: CollectionReference = firestore.collection("users")
        users.document(user.id.toString()).set(User.serialize(user))
    }

    suspend fun clearLocalUserVote(context: Context): Boolean{
        val user: User = LocalUtil.loadLocalUser(context)
        val users: CollectionReference = firestore.collection("users")
        val userRef = users.document(user.id.toString())
        firestore.runBatch {
            userRef.update("vote", -1)
            userRef.update("hasVoted", false)
        }.await()
        return true
    }

    suspend fun subscribeLobby(context: Context, lobby: MutableLiveData<Lobby>,
                               viewModel: LobbyViewModel
    ): ListenerRegistration{
        val query = firestore.collection("lobbies").document(lobby.value!!.code)
        Log.d("REMOTE_DATA_SOURCE","Subscribing to ${lobby.value!!.code}")
        val registration = query.addSnapshotListener{ snapshot, e ->
            Log.d("REMOTE_DATA_SOURCE","Subscription update ${snapshot == null}")
            if (snapshot != null && snapshot.exists()) {
                Log.d("REMOTE_DATA_SOURCE","Subscription update entered")
                viewModel.updateLobby(snapshot)
                Log.d("REMOTE_DATA_SOURCE", "Execution finished")
            }
        }
        return registration
    }

    suspend fun subscribeUsers(context: Context, lobby: Lobby,
                               userList: MutableLiveData<MutableMap<UUID, ListenerRegistration>>,
                               viewModel: LobbyViewModel){
        val editedUserList: MutableSet<UUID>
        if(userList.value != null && userList.value!!.keys.isNotEmpty()){
            Log.d("REMOTE_DATA_SOURCE", "userList not null but: ${userList.value == null}")
            editedUserList = userList.value!!.keys
        }else{
            Log.d("REMOTE_DATA_SOURCE", "userList null but: ${userList.value == null}")
            editedUserList = mutableSetOf()
            val newMap: MutableMap<UUID, ListenerRegistration> = mutableMapOf()
            userList.value = newMap
            Log.d("REMOTE_DATA_SOURCE", "now check it out: ${userList.value == null}")

        }
        val pendingSubscription: MutableList<UUID> = mutableListOf()
        for(user in lobby.users){
            if(editedUserList.contains(user.id)){
                editedUserList.remove(user.id)
            }else{
                pendingSubscription.add(user.id)
            }
        }

        // Remove ListenerRegistration of the disconnected player
        // if ListenerRegistration is not null
        for(remainingUser in editedUserList) {
            userList.value!![remainingUser]?.remove()
            userList.value!!.remove(remainingUser)
        }
        val usersRef: CollectionReference = firestore.collection("users")

        firestore.runBatch{ batch ->
            for(user in pendingSubscription){
                Log.d("REMOTE_DATA_SOURCE", "is null: ${userList.value == null}")
                userList.value!!.put(user, usersRef.document(user.toString()).addSnapshotListener{
                    snapshot, e ->
                        viewModel.reloadLobby()
                })
            }
        }.await()


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
                        lobby = Lobby.loadSnapshot(context, existingLobby)
                        break
                    }
                }
            }
        }

        if(lobby != null){
            addLocalUser(context)
            val userList: MutableList<User> = lobby.users.toMutableList()
            val localUser: User = LocalUtil.loadLocalUser(context)

            userList.add(localUser)
            lobby.users = userList.toList()

            val serializedLobby: SerializedLobby = Lobby.serialize(lobby)
            lobbies.document(lobby.code).update("users", serializedLobby.users).await()
            return getLobby(context, lobby.code)
        }else{
            // TODO: Join failed
            return null
        }
    }

    suspend fun getLobby(context: Context, code: String): Lobby?{
        val lobbies: CollectionReference = firestore.collection("lobbies")
        var addedLobby = lobbies.document(code).get().await()
        if(addedLobby != null){
            return Lobby.loadSnapshot(context, addedLobby)
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

    suspend fun vote(context: Context, value: Int): Boolean{
        val localUser: User = LocalUtil.loadLocalUser(context)
        val users: CollectionReference = firestore.collection("users")
        Log.d("VOTE", "userid: ${localUser.id.toString()}")
        val user = users.document(localUser.id.toString())

        firestore.runBatch{ batch ->
            user.update("vote", value)
            user.update("hasVoted", true)
        }.await()

        return true
    }

    suspend fun showResults(context: Context, lobby: Lobby, value: Boolean): Boolean{
        val lobbies: CollectionReference = firestore.collection("lobbies")
        lobbies.document(lobby.code).update("showResults", value).await()
        if(!value)
            resetVotes(context, lobby)
        return true
    }

    suspend fun resetVotes(context: Context, lobby: Lobby){
        val usersRef: CollectionReference = firestore.collection("users")

        val lobby: Lobby? = getLobby(context, lobby.code)
        if(lobby != null){
            val users = lobby.users
            firestore.runBatch { batch ->
                for(user in users){
                    usersRef.document(user.id.toString()).update("vote", -1)
                    usersRef.document(user.id.toString()).update("hasVoted", false)
                }
            }.await()
        }

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

    suspend fun createLobby(context: Context, pendingLobby: Lobby) : Lobby{
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

        addLocalUser(context)
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