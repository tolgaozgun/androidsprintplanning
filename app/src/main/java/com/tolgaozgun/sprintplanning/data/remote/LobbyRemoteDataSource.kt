package com.tolgaozgun.sprintplanning.data.remote

import android.util.Log
import androidx.lifecycle.liveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.tolgaozgun.sprintplanning.data.local.LobbyDatabase
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.repository.LobbyRepository
import com.tolgaozgun.sprintplanning.util.FirebaseUtil
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

    suspend fun createLobby(pendingLobby: Lobby) : Lobby{

        val lobbies : CollectionReference = firestore.collection("lobbies")
        val documentReference : DocumentReference = lobbies.add(pendingLobby)
            .addOnSuccessListener { snapshot ->
                Log.d("CREATELOBBY", "Successfully added lobby with id ${snapshot.id}")
            }
            .addOnFailureListener { exception ->
                Log.d("CREATELOBBY", "Failed to add lobby $exception")
            }.await()
        return pendingLobby
    }




}