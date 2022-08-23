package com.tolgaozgun.sprintplanning.viewmodels.mainmenu

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.tolgaozgun.sprintplanning.repository.LobbyRepository
import com.tolgaozgun.sprintplanning.viewmodels.TransactionViewModel
import java.util.*

class MainViewModel(
    fragmentManager: FragmentManager,
    private val lobbyRepository: LobbyRepository,
)   : TransactionViewModel(fragmentManager = fragmentManager) {

    fun checkFirstTime(context: Context){
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("local_user", Context.MODE_PRIVATE)

        val idValue: String? = sharedPreferences.getString("id", null)
        if(idValue == null || idValue.trim().isEmpty()){
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            val userId: UUID = UUID.randomUUID()
            editor.putString("id", userId.toString())
            editor.apply()
        }
    }

    fun checkLobby(context: Context){
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("lobby", Context.MODE_PRIVATE)
        if(sharedPreferences.contains("current")){
            val lobbyCode: String = sharedPreferences.getString("current", null)!!


        }
    }
}