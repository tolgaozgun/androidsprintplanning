package com.tolgaozgun.sprintplanning.util

import android.content.Context
import android.content.SharedPreferences
import androidx.room.ColumnInfo
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.data.model.User
import java.util.*

class LocalUtil {

    companion object{
        fun loadLocalUser(context: Context): User{
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("local_user", Context.MODE_PRIVATE)
            val idString: String = sharedPreferences.getString("id", null)!!
            val id: UUID = UUID.fromString(idString)
            val name: String = sharedPreferences.getString("name", null)!!
            val avatarUrl: String = sharedPreferences.getString("avatar", "")!!
            val vote: Int = sharedPreferences.getInt("vote", -1)
            val hasVoted: Boolean = sharedPreferences.getBoolean("hasVoted", false)

            return User(id, name, avatarUrl, vote, hasVoted)
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