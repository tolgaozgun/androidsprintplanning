package com.tolgaozgun.sprintplanning.util

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.Gson
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.data.model.LobbyState
import com.tolgaozgun.sprintplanning.data.model.User
import java.util.*

class Converters {

    @TypeConverter
    fun uuidListToString(value: List<UUID>): String {
        val gson: Gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun stringToUuidList(value: String): List<UUID>{
        val listType = object : TypeToken<List<UUID>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun userListToString(value: List<User>): String {
        val gson: Gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun stringToUserList(value: String): List<User>{
        val listType = object : TypeToken<List<User>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun userToString(value: User): String {
        val gson: Gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun stringToUser(value: String): User {
        val userType = object : TypeToken<User>() {}.type
        return Gson().fromJson(value, userType)
    }

    companion object{
        fun uuidListToString(value: List<UUID>): String {
            val gson: Gson = Gson()
            return gson.toJson(value)
        }

        fun stringToUuidList(value: String): List<UUID>{
            val listType = object : TypeToken<List<UUID>>() {}.type
            return Gson().fromJson(value, listType)
        }

        fun userListToString(value: List<User>): String {
            val gson: Gson = Gson()
            return gson.toJson(value)
        }

        fun stringToUserList(value: String): List<User>{
            val listType = object : TypeToken<List<User>>() {}.type
            return Gson().fromJson(value, listType)
        }

        fun userToString(value: User): String {
            val gson: Gson = Gson()
            return gson.toJson(value)
        }

        fun stringToUser(value: String): User {
            val userType = object : TypeToken<com.tolgaozgun.sprintplanning.data.model.User>() {}.type
            return Gson().fromJson(value, userType)
        }

        fun snapshotToUser(snapshot: DocumentSnapshot): User {
            val map: Map<String, Any> = snapshot.data as Map<String, Any>

            val idString: String = map["id"] as String
            val name: String = map["name"] as String
            val avatarUrl: String = map["avatar"] as String
            val vote: Int = (map["vote"] as Long).toInt()
            val hasVoted: Boolean = map["has_voted"] as Boolean

            return User(UUID.fromString(idString), name, avatarUrl, vote, hasVoted)
        }

        fun snapshotToLobby(snapshot: DocumentSnapshot): Lobby {
            val map: Map<String, Any> = snapshot.data as Map<String, Any>

            val idString: String = map["id"] as String
            val code: String = map["code"] as String
            val name: String = map["name"] as String
            val userLimit: Int = (map["userLimit"] as Long).toInt()
            val timeCreated: Long = map["timeCreated"] as Long
            val timeUpdated: Long = map["timeUpdated"] as Long
            val stringList: List<String> = map["users"] as List<String>
            val status: String = map["status"] as String
            val askToJoin: Boolean = map["askToJoin"] as Boolean

            val convertedId: UUID = UUID.fromString(idString)
            val lobbyState: LobbyState = LobbyState.valueOf(status)
            val userList: MutableList<User> = mutableListOf()
            for(userString in stringList){
                userList.add(stringToUser(userString))
            }
            return Lobby(convertedId, code, name, userLimit, timeCreated, timeUpdated,
                userList.toList(), lobbyState, askToJoin)
        }

    }


}