package com.tolgaozgun.sprintplanning.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ListenerRegistration
import com.tolgaozgun.sprintplanning.data.local.LobbyDatabase
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.data.model.User
import com.tolgaozgun.sprintplanning.data.remote.LobbyRemoteDataSource
import com.tolgaozgun.sprintplanning.views.lobby.LobbyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    suspend fun joinLobby(id: String, context: Context) : Lobby?{
        return remoteDataSource.joinLobby(id, context)
    }

    suspend fun createLobby(context: Context, pendingLobby: Lobby) : Lobby{
        return remoteDataSource.createLobby(context, pendingLobby)
    }

    suspend fun voteLobby(context: Context, vote: Int): Boolean {
        return remoteDataSource.vote(context, vote)
    }

    suspend fun subscribeLobby(context: Context, lobby: MutableLiveData<Lobby>,
                               viewModel: LobbyViewModel): ListenerRegistration{
        return remoteDataSource.subscribeLobby(context, lobby, viewModel)
    }

    suspend fun loadUsersWithString(stringList: List<String>): List<User>{
        val uuidList: MutableList<UUID> = mutableListOf()

        for(string in stringList){
            uuidList.add(UUID.fromString(string))
        }
        return loadUsersWithUuid(uuidList.toList())
    }

    suspend fun loadUsersWithUuid(uuidList: List<UUID>): List<User>{
        return remoteDataSource.loadUsers(uuidList)
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

}