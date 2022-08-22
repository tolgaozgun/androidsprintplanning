package com.tolgaozgun.sprintplanning.data.model

import androidx.room.*
import com.google.common.base.Converter
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
//    @ColumnInfo(name="users")               var users: List<UUID>,
    @ColumnInfo(name="status")              var status: LobbyState,
    @ColumnInfo(name="ask_to_join")         var askToJoin: Boolean,
)