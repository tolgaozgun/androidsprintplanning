package com.tolgaozgun.sprintplanning.util

import android.os.Bundle
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.data.model.LobbyState
import java.util.*

class LobbyUtil {

    companion object{
        private var MIN_LETTER_COUNT = 2
        private var MIN_NUMBER_COUNT = 2
        private var CODE_LENGTH = 6

        fun createCode(): String{
            var code: String = ""
            var letterCount: Int = 0
            var numberCount: Int = 0
            for(i in 1..CODE_LENGTH){
                var requiredLetters = MIN_LETTER_COUNT - letterCount
                var requiredNumbers = MIN_NUMBER_COUNT - numberCount
                var remainingSpots = CODE_LENGTH - i
                if(remainingSpots <= requiredLetters){
                    code += randomLetter()
                }else if (remainingSpots  <= requiredNumbers){
                    code += randomNumber()
                }else{
                    code += randomChar()
                }
            }
            return code
        }

        private fun randomChar(): Char{
            return (('A' .. 'Z') + ('0' .. '9')).random()
        }

        private fun randomLetter(): Char{
            return ('A' .. 'Z').random()
        }

        private fun randomNumber(): Char{
            return ('0' .. '9').random()
        }

        fun createBundle(lobby: Lobby) : Bundle {
            val bundle: Bundle = Bundle()
            bundle.putString("name", lobby.name)
            bundle.putString("id", lobby.id.toString())
            bundle.putString("code", lobby.code)
            bundle.putInt("user_limit", lobby.userLimit)
            bundle.putLong("time_created", lobby.timeCreated)
            bundle.putLong("time_updated", lobby.timeUpdated)
            bundle.putString("status", lobby.status.toString())
            bundle.putBoolean("ask_to_join", lobby.askToJoin)
            return bundle
        }

        fun fromBundle(bundle: Bundle): Lobby{
            val name: String = bundle.getString("name")!!
            val id: UUID = UUID.fromString(bundle.getString("id"))
            val code: String = bundle.getString("code")!!
            val userLimit: Int = bundle.getInt("user_limit")
            val timeCreated: Long = bundle.getLong("time_created")
            val timeUpdated: Long = bundle.getLong("time_updated")
            val status: LobbyState = LobbyState.valueOf(bundle.getString("status")!!)
            val askToJoin: Boolean = bundle.getBoolean("ask_to_join")

            return Lobby(id, name, code, userLimit, timeCreated, timeUpdated, status, askToJoin)
        }
    }


}