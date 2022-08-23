package com.tolgaozgun.sprintplanning.repository

import android.content.Context
import com.tolgaozgun.sprintplanning.data.local.LobbyDatabase
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.data.model.User
import com.tolgaozgun.sprintplanning.data.remote.LobbyRemoteDataSource
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

    suspend fun createLobby(pendingLobby: Lobby) : Lobby{
        return remoteDataSource.createLobby(pendingLobby)
    }

    suspend fun voteLobby(vote: Int, userIdString: String): Boolean {
        return remoteDataSource.vote(vote, userIdString)
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

}