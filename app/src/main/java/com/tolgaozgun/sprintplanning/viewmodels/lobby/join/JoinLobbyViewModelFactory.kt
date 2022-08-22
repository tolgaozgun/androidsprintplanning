package com.tolgaozgun.sprintplanning.viewmodels.lobby.join

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tolgaozgun.sprintplanning.repository.LobbyRepository

class JoinLobbyViewModelFactory(private val context: Context,
                                private val fragmentManager: FragmentManager,
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(JoinLobbyViewModel::class.java)){
            val lobbyRepository: LobbyRepository = LobbyRepository.getInstance(context = context)
            JoinLobbyViewModel(context, fragmentManager, lobbyRepository) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel class, not a JoinLobbyViewModel")
        }
    }
}