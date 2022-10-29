package com.tolgaozgun.sprintplanning.data.local

import androidx.room.*
import com.tolgaozgun.sprintplanning.data.model.Lobby
import java.util.*

@Dao
interface LobbyDao {

    @Insert
    fun insertLobby(vararg lobby: Lobby)

    @Update
    fun updateLobby(vararg lobby: Lobby)

    @Delete
    fun deleteLobby(vararg lobby: Lobby)

    @Query("SELECT * FROM Lobby WHERE ID = :id")
    fun loadLobby(vararg id: UUID) : Lobby

}