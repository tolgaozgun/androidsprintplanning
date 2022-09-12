package com.tolgaozgun.sprintplanning.repository

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.tolgaozgun.sprintplanning.data.local.LobbyDatabase
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.data.model.User
import com.tolgaozgun.sprintplanning.data.remote.LobbyRemoteDataSource
import com.tolgaozgun.sprintplanning.viewmodels.lobby.LobbyViewModel
import java.util.*

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

class LobbyRepository(
    private val localDataSource: LobbyDatabase,
    private val remoteDataSource: LobbyRemoteDataSource,
) {

    companion object{
        @Volatile
        private var INSTANCE : LobbyRepository? = null

        fun getInstance(context: Context): LobbyRepository {
            if(INSTANCE == null){
                synchronized(this){
                    if(INSTANCE == null){
                        INSTANCE = LobbyRepository(LobbyDatabase.getDatabase(context = context), LobbyRemoteDataSource.getInstance())
                    }
                }
            }
            return INSTANCE!!
        }
    }

    suspend fun joinLobby(id: String, context: Context, isRejoining: Boolean = false) : Lobby?{
        return remoteDataSource.joinLobby(id, context, isRejoining)
    }

    suspend fun createLobby(context: Context, pendingLobby: Lobby) : Lobby?{
        return remoteDataSource.createLobby(context, pendingLobby)
    }

    suspend fun voteLobby(context: Context, vote: Int, code: String): Boolean {
        return remoteDataSource.vote(context, vote, code)
    }

    suspend fun subscribeLobby(context: Context, lobby: MutableLiveData<Lobby>,
                               viewModel: LobbyViewModel
    ): ListenerRegistration{
        return remoteDataSource.subscribeLobby(context, lobby, viewModel)
    }

    suspend fun loadUsersWithString(context: Context, userList: List<User>?, stringList: List<String>, shouldLoadAvatar: Boolean = false): List<User>{
        val uuidList: MutableList<UUID> = mutableListOf()

        for(string in stringList){
            uuidList.add(UUID.fromString(string))
        }
        return loadUsersWithUuid(context, userList, uuidList.toList(), shouldLoadAvatar)
    }

    suspend fun loadUsersWithUuid(context: Context, userList: List<User>?, uuidList: List<UUID>, shouldLoadAvatar: Boolean = false): List<User>{
        return remoteDataSource.loadUsers(context, userList, uuidList, shouldLoadAvatar)
    }

    suspend fun updateUser(context: Context): Boolean{
        return remoteDataSource.updateUser(context)
    }

    suspend fun showResults(context: Context, lobby: Lobby, value: Boolean): Boolean{
        return remoteDataSource.showResults(context, lobby, value)
    }

    suspend fun leaveLobby(context: Context, lobby: Lobby): Boolean{
        return remoteDataSource.leaveLobby(context, lobby)
    }

    suspend fun addLocalUser(context: Context){
        return remoteDataSource.addLocalUser(context)
    }

    suspend fun subscribeUsers(context: Context, lobby: MutableLiveData<Lobby>,
                               users: MutableLiveData<MutableMap<UUID, ListenerRegistration>>,
                               viewModel: LobbyViewModel){
        return remoteDataSource.subscribeUsers(context, lobby, users, viewModel)
    }

    suspend fun getLobby(context: Context, code: String): Lobby?{
        return remoteDataSource.getLobby(context, code)
    }

    suspend fun uploadFile(context: Context, file: Uri): String?{
        return remoteDataSource.uploadFile(context, file)
    }

    suspend fun updateLobbyUser(context: Context, lobby: MutableLiveData<Lobby>,
                                snapshot: DocumentSnapshot){
        return remoteDataSource.updateLobbyUser(context, lobby, snapshot)
    }

}