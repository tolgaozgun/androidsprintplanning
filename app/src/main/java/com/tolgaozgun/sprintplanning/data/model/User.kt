package com.tolgaozgun.sprintplanning.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.util.*

@Entity
data class User(
    @PrimaryKey                         var id: UUID,
    @ColumnInfo(name="name")            var name: String = "Name",
    @ColumnInfo(name="avatar_url")      var avatarUrl: String = "local/face1",
    @ColumnInfo(name="vote")            var vote: Int = -1,
    @ColumnInfo(name="hasVoted")        var hasVoted: Boolean,
){
    companion object{
        fun serialize(user: User): SerializedUser{
            with(user){
                return SerializedUser(id.toString(), name, avatarUrl, vote, hasVoted)
            }
        }

        fun deSerialize(serializedUser: SerializedUser): User{
            with(serializedUser){
                return User(UUID.fromString(id), name, avatarUrl, vote, hasVoted)
            }
        }
    }

}



data class SerializedUser(
    var id: String,
    var name: String = "Name",
    var avatarUrl: String = "local/face1",
    var vote: Int = -1,
    var hasVoted: Boolean = false,
)





