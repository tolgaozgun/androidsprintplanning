package com.tolgaozgun.sprintplanning.util

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.firebase.firestore.auth.User
import com.google.gson.Gson
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
    }


}