package com.tolgaozgun.sprintplanning.viewmodels.lobby.join

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.viewModelScope
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.repository.LobbyRepository
import com.tolgaozgun.sprintplanning.util.LobbyUtil
import com.tolgaozgun.sprintplanning.viewmodels.TransactionViewModel
import com.tolgaozgun.sprintplanning.views.lobby.LobbyFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JoinLobbyViewModel(
    private var context: Context,
    fragmentManager: FragmentManager,
    private var lobbyRepository: LobbyRepository)
    : TransactionViewModel(fragmentManager = fragmentManager) {

    fun joinRoom(input: String){

        var code: String = input.trim().uppercase()
        viewModelScope.launch(Dispatchers.IO){
            val result: Lobby? = lobbyRepository.joinLobby(code, context)
            when(result){
                is Lobby ->{
                    Log.d("LOBBY_JOIN", "Lobby is loaded, with no of users: ${result.users.count()}")
                    replaceFragment(
                        fragment = LobbyFragment(),
                        shouldAddToBackStack = true,
                        arguments = LobbyUtil.createBundle(result)
                    )
                }
                else ->
                    Log.d("JOIN_LOBBY", "Failed to join")
                    // Error
//                    Toast.makeText(context, "Error while joining, no room found", Toast.LENGTH_LONG).show()
            }

        }

    }

}