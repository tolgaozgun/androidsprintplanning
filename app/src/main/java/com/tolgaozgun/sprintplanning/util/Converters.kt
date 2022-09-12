package com.tolgaozgun.sprintplanning.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.Gson
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.data.model.LobbyState
import com.tolgaozgun.sprintplanning.data.model.User
import com.tolgaozgun.sprintplanning.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.net.URL
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
    fun bitmapToString(value: Bitmap): String {
        val baos: ByteArrayOutputStream = ByteArrayOutputStream()
        value.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val byteArray: ByteArray = baos.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    @TypeConverter
    fun stringToBitmap(value: String): Bitmap {
        val imageAsBytes: ByteArray = Base64.decode(value.toByteArray(), Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
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
            val userType = object : TypeToken<User>() {}.type
            return Gson().fromJson(value, userType)
        }

        fun bitmapToString(value: Bitmap): String {
            val baos: ByteArrayOutputStream = ByteArrayOutputStream()
            value.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val byteArray: ByteArray = baos.toByteArray()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        }

        fun stringToBitmap(value: String): Bitmap {
            val imageAsBytes: ByteArray = Base64.decode(value.toByteArray(), Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
        }

        suspend fun snapshotToUser(context: Context, snapshot: DocumentSnapshot,
                                   shouldLoadAvatar: Boolean = false, userRepository: UserRepository): User {
            val map: Map<String, Any> = snapshot.data as Map<String, Any>

            val idString: String = map["id"] as String
            val name: String = map["name"] as String


            val avatarUrl: String = if(map["avatarUrl"] == null){
                ""
            }else{
                map["avatarUrl"] as String
            }
            val vote: Int = (map["vote"] as Long).toInt()
            val hasVoted: Boolean = map["hasVoted"] as Boolean
            val hasChangedVote: Boolean = map["hasChangedVote"] as Boolean

            var image: Bitmap? = ImageUtil.getDefaultBitmap(context)

            Log.d("RETRIEVE_IMAGE", "ShouldLoadAvatar: $shouldLoadAvatar")
            if(shouldLoadAvatar){
                Log.d("RETRIEVE_IMAGE", "Inside ShouldLoadAvatar for $name")
                withContext(Dispatchers.IO){
                    image = userRepository.retrieveImage(avatarUrl)
                    Log.d("RETRIEVE_IMAGE", "Retrieved image with $avatarUrl")
                    Log.d("RETRIEVE_IMAGE", "${image == null}")
                }
            }

            return User(UUID.fromString(idString), name, avatarUrl, vote, hasVoted, hasChangedVote, image)
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
            val showResults: Boolean = map["showResults"] as Boolean

            val convertedId: UUID = UUID.fromString(idString)
            val lobbyState: LobbyState = LobbyState.valueOf(status)
            val userList: MutableList<User> = mutableListOf()
            for(userString in stringList){
                userList.add(stringToUser(userString))
            }
            return Lobby(convertedId, code, name, timeCreated, timeUpdated,
                userList.toList(), lobbyState, showResults)
        }

    }


}