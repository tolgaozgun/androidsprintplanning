package com.tolgaozgun.sprintplanning.util

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.data.model.LobbyState
import com.tolgaozgun.sprintplanning.data.model.User
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
            Log.d("CODE","Starting code $code")
            for(i in 1..CODE_LENGTH){
                Log.d("CODE","Iteration: $i code $code")
                var requiredLetters = MIN_LETTER_COUNT - letterCount
                var requiredNumbers = MIN_NUMBER_COUNT - numberCount
                var remainingSpots = CODE_LENGTH - i
                if(remainingSpots <= requiredLetters){
                    code += randomLetter()
                    letterCount++
                }else if (remainingSpots  <= requiredNumbers){
                    code += randomNumber()
                    numberCount++
                }else{
                    if(generator(2) > 0){
                        code += randomLetter()
                        letterCount++
                    }else{
                        code += randomNumber()
                        numberCount++
                    }
                }
            }
            return code
        }

        private fun generator(range: Int): Int{
            val random: Random = Random()
            return random.nextInt(range)
        }

        private fun randomChar(): Char{
            val list = ('A' .. 'Z') + ('0' .. '9')
            return list[generator(list.count())]
        }

        private fun randomLetter(): Char{
            val list = ('A' .. 'Z').toList()
            return list[generator(list.count())]
        }

        private fun randomNumber(): Char{
            val list = ('0' .. '9').toList()
            return list[generator(list.count())]
        }

        fun createBundle(lobby: Lobby) : Bundle {
            val bundle: Bundle = Bundle()
            with(lobby){
                bundle.putString("name", name)
                bundle.putString("id", id.toString())
                bundle.putString("code", code)
                bundle.putInt("user_limit", userLimit)
                bundle.putLong("time_created", timeCreated)
                bundle.putLong("time_updated", timeUpdated)
                bundle.putString("status", status.toString())
                bundle.putString("users", Converters.userListToString(users))
                bundle.putBoolean("ask_to_join", askToJoin)
                bundle.putBoolean("show_results", showResults)
                Log.d("BUNDLE_CREATE", "Code: $code and name: $name")
            }
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
            val users: List<User> = Converters.stringToUserList(bundle.getString("users")!!)
            val showResults: Boolean = bundle.getBoolean("show_results")

            return Lobby(id, code, name, userLimit, timeCreated, timeUpdated, users, status,
                askToJoin, showResults)
        }

        fun createQRCode(content: String): Bitmap{
            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
                }
            }
            return bitmap
        }
    }


}