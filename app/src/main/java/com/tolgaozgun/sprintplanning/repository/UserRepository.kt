package com.tolgaozgun.sprintplanning.repository

import android.content.Context
import android.graphics.Bitmap
import com.tolgaozgun.sprintplanning.data.local.LobbyDatabase
import com.tolgaozgun.sprintplanning.data.remote.LobbyRemoteDataSource
import com.tolgaozgun.sprintplanning.data.remote.UserRemoteDataSource

class UserRepository(
    private val remoteDataSource: UserRemoteDataSource,
){


    companion object{
        @Volatile
        private var INSTANCE : UserRepository? = null

        fun getInstance(context: Context): UserRepository {
            if(INSTANCE == null){
                synchronized(this){
                    if(INSTANCE == null){
                        INSTANCE = UserRepository(UserRemoteDataSource.getInstance())
                    }
                }
            }
            return INSTANCE!!
        }
    }

    suspend fun retrieveImage(url: String): Bitmap?{
        return remoteDataSource.retrieveAvatar(url)
    }
}