package com.tolgaozgun.sprintplanning.util

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.room.ColumnInfo
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.data.model.User
import com.tolgaozgun.sprintplanning.data.remote.LobbyRemoteDataSource
import com.tolgaozgun.sprintplanning.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import java.util.*

class LocalUtil {

    companion object{


        fun loadLocalUser(context: Context): User{
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("local_user", Context.MODE_PRIVATE)
            val idString: String = sharedPreferences.getString("id", null)!!
            val id: UUID = UUID.fromString(idString)
            val name: String = sharedPreferences.getString("name", null)!!
            val avatarUrl: String = sharedPreferences.getString("avatarUrl", "")!!
            val vote: Int = sharedPreferences.getInt("vote", -1)
            val hasVoted: Boolean = sharedPreferences.getBoolean("hasVoted", false)
            val hasChangedVote: Boolean = sharedPreferences.getBoolean("hasChangedVote", false)

            return User(id, name, avatarUrl, vote, hasVoted, hasChangedVote)
        }

        suspend fun loadLocalUserWithAvatar(context: Context, userRepository: UserRepository,
                                            shouldLoadAvatar: Boolean): User{
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("local_user", Context.MODE_PRIVATE)
            val idString: String = sharedPreferences.getString("id", null)!!
            val id: UUID = UUID.fromString(idString)
            val name: String = sharedPreferences.getString("name", null)!!
            val avatarUrl: String = sharedPreferences.getString("avatarUrl", "")!!
            val vote: Int = sharedPreferences.getInt("vote", -1)
            val hasVoted: Boolean = sharedPreferences.getBoolean("hasVoted", false)
            val hasChangedVote: Boolean = sharedPreferences.getBoolean("hasChangedVote", false)
            var image: Bitmap? = ImageUtil.getDefaultBitmap(context)

            if(shouldLoadAvatar){
                withContext(Dispatchers.IO){
                    image = userRepository.retrieveImage(avatarUrl)
                }
            }

            return User(id, name, avatarUrl, vote, hasVoted, hasChangedVote, image)
        }

        fun addLobbyToLocal(context: Context, code: String): Boolean{
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("lobby", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()){
                putString("code", code)
                apply()
            }
            return true
        }

        fun getLocalLobbyCode(context: Context): String?{
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("lobby", Context.MODE_PRIVATE)
            return sharedPreferences.getString("code", null)
        }

        fun isJoinedLobby(context: Context): Boolean{
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("lobby", Context.MODE_PRIVATE)
            val code: String? = sharedPreferences.getString("code", null)
            return code != null && code.trim().isNotEmpty()
        }

        fun setAvatarUrl(context: Context, url: String): Boolean{
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("local_user", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()){
                putString("avatarUrl", url)
                apply()
            }
            return true
        }

        fun removeLobbyFromLocal(context: Context): Boolean{
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("lobby", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()){
                remove("code")
                apply()
            }
            return true
        }
    }
}