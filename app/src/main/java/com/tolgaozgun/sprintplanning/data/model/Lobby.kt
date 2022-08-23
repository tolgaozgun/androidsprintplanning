package com.tolgaozgun.sprintplanning.data.model

import androidx.room.*
import com.google.firebase.firestore.DocumentSnapshot
import com.tolgaozgun.sprintplanning.util.Converters
import java.util.*

@Entity(primaryKeys = ["id", "code"])
data class Lobby(
    @ColumnInfo(name="id")                  var id: UUID,
    @ColumnInfo(name="code")                var code: String,
    @ColumnInfo(name="name")                var name: String,
    @ColumnInfo(name="user_limit")          var userLimit: Int,
    @ColumnInfo(name="time_created")        var timeCreated: Long,
    @ColumnInfo(name="time_updated")        var timeUpdated: Long,
    @ColumnInfo(name="users")
    @TypeConverters(Converters::class)      var users: List<UUID>,
    @ColumnInfo(name="status")              var status: LobbyState,
    @ColumnInfo(name="ask_to_join")         var askToJoin: Boolean,
){
    companion object{

        fun serialize(lobby: Lobby): SerializedLobby{
            with(lobby){
                var userList: MutableList<String> = mutableListOf()
                for(userId in users){
                    userList.add(userId.toString())
                }
                return SerializedLobby(id.toString(), code, name, userLimit, timeCreated,
                    timeUpdated, userList.toList(), status.toString(), askToJoin)
            }
        }

        fun deSerialize(serializedLobby: SerializedLobby): Lobby{
            with(serializedLobby){
                val convertedId: UUID = UUID.fromString(id)

                val userList: MutableList<UUID> = mutableListOf()
                for(idString in users){
                    userList.add(UUID.fromString(idString))
                }

                val lobbyState: LobbyState = LobbyState.valueOf(status)

                return Lobby(convertedId, code, name, userLimit, timeCreated, timeUpdated,
                    userList.toList(), lobbyState, askToJoin)
            }
        }

        fun loadSnapshot(snapshot: DocumentSnapshot): Lobby{
            val map: Map<String, Any> = snapshot.data as Map<String, Any>

            val idString: String = map["id"] as String
            val code: String = map["code"] as String
            val name: String = map["name"] as String
            val userLimit: Int = map["userLimit"] as Int
            val timeCreated: Long = map["timeCreated"] as Long
            val timeUpdated: Long = map["timeUpdated"] as Long
            val stringList: List<String> = map["users"] as List<String>
            val status: String = map["status"] as String
            val askToJoin: Boolean = map["askToJoin"] as Boolean

            val convertedId: UUID = UUID.fromString(idString)
            val lobbyState: LobbyState = LobbyState.valueOf(status)
            val userList: MutableList<UUID> = mutableListOf()
            for(idString in stringList){
                userList.add(UUID.fromString(idString))
            }
            return Lobby(convertedId, code, name, userLimit, timeCreated, timeUpdated,
                userList.toList(), lobbyState, askToJoin)
        }
    }



}

class SerializedLobby(
    var id: String,
    var code: String,
    var name: String,
    var userLimit: Int,
    var timeCreated: Long,
    var timeUpdated: Long,
    var users: List<String>,
    var status: String,
    var askToJoin: Boolean
)