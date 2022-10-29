package com.tolgaozgun.sprintplanning.viewmodels.mainmenu

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.viewModelScope
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.repository.LobbyRepository
import com.tolgaozgun.sprintplanning.util.LobbyUtil
import com.tolgaozgun.sprintplanning.util.LocalUtil
import com.tolgaozgun.sprintplanning.viewmodels.TransactionViewModel
import com.tolgaozgun.sprintplanning.views.lobby.LobbyFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
            with(sharedPreferences.edit()){
                val userId: UUID = UUID.randomUUID()
                putString("id", userId.toString())
                apply()
            }
        }

        val name: String? = sharedPreferences.getString("name", null)
        if(name == null || name.trim().isEmpty()){
            with(sharedPreferences.edit()){
                putString("name", "Name")
                apply()
            }
        }
        viewModelScope.launch(Dispatchers.IO){
            lobbyRepository.updateUser(context)
        }

    }

    fun checkLobby(context: Context){
        if(LocalUtil.isJoinedLobby(context)){
            val code: String = LocalUtil.getLocalLobbyCode(context)!!
            viewModelScope.launch {
                when(val lobby: Lobby? = lobbyRepository.joinLobby(code, context)){
                    is Lobby -> {
                        Toast.makeText(context, "You were joined back to your previous lobby", Toast.LENGTH_SHORT).show()
                        replaceFragment(
                            fragment = LobbyFragment(),
                            shouldAddToBackStack = true,
                            arguments = LobbyUtil.createBundle(lobby)
                        )
                    }
                    else -> {
                        Toast.makeText(context, "Your previous lobby was disbanded, cannot join.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}