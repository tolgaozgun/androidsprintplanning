//package com.tolgaozgun.sprintplanning.data.remote
//
//import android.util.Log
//import com.google.firebase.firestore.FirebaseFirestore
//import com.tolgaozgun.sprintplanning.data.model.Lobby
//import com.tolgaozgun.sprintplanning.data.model.LobbyState
//import com.tolgaozgun.sprintplanning.data.model.User
//import com.tolgaozgun.sprintplanning.util.FirebaseUtil
//import java.util.*
//
//class LobbyApi {
//
//    private val TAG = "FIRESTORE"
//
//    fun createLobby() {
//        Log.d(TAG, "Create lobby triggerred")
//
//        // TODO: Remove this after testing
//        val listOfUsers : MutableList<User> = mutableListOf<User>()
//
//        listOfUsers.add(User(UUID.randomUUID(), "Name", "Avatar", null, false))
//        listOfUsers.add(User(UUID.randomUUID(), "Name2", "Avatar2", null, false))
//
//        val roomId: UUID = UUID.randomUUID()
//        val roomName: String = "Room"
//        val timeCreated: Long = System.currentTimeMillis()
//        val timeUpdated: Long = timeCreated
//        val users = mutableListOf<User>()
//        val status = LobbyState.WAITING
//
//        val hashMap = hashMapOf<String, Any>(
//            "id" to roomId,
//            "name" to roomName,
//            "timeCreated" to timeCreated,
//            "timeUpdated" to timeUpdated,
//            "users" to listOfUsers,
//            "status" to status,
//        )
//
//
//        FirebaseUtil().getFirestore().collection("lobbies")
//            .add(hashMap)
//            .addOnSuccessListener {
//                Log.d(TAG,"Added document with id ${it.id}" )
//            }.addOnFailureListener { exception ->
//                Log.d(TAG, "Error adding document $exception")
//            }
//
//        Log.d(TAG, "Create lobby ended")
//
//    }
//
//
//
//}