package com.tolgaozgun.sprintplanning.util

import android.content.Context
import android.content.SharedPreferences
import androidx.room.ColumnInfo
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
            val avatarUrl: String? = sharedPreferences.getString("avatar", null)
            val vote: Int = sharedPreferences.getInt("vote", -1)
            val hasVoted: Boolean = sharedPreferences.getBoolean("has_voted", false)

            return User(id, name, avatarUrl, vote, hasVoted)
        }
    }
}