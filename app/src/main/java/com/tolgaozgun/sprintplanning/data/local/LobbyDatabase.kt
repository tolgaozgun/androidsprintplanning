package com.tolgaozgun.sprintplanning.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.data.model.User
import com.tolgaozgun.sprintplanning.util.Converters

@Database(
    entities = [
        Lobby::class,
        User::class
    ],
    version = 2
)
@TypeConverters(Converters::class)
abstract class LobbyDatabase : RoomDatabase() {
    abstract fun lobbyDao(): LobbyDao

    companion object{
        @Volatile
        private var INSTANCE: LobbyDatabase? = null

        fun getDatabase(context: Context): LobbyDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LobbyDatabase::class.java,
                    "lobby_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}