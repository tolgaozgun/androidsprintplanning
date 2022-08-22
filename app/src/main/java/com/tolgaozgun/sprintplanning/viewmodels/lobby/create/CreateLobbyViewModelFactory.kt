package com.tolgaozgun.sprintplanning.viewmodels.lobby.create

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tolgaozgun.sprintplanning.repository.LobbyRepository

class CreateLobbyViewModelFactory(
    private val context: Context,
    private val fragmentManager: FragmentManager,
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CreateLobbyViewModel::class.java)){
            val lobbyRepository: LobbyRepository = LobbyRepository.getInstance(context = context)
            CreateLobbyViewModel(fragmentManager = fragmentManager,
                context = context, lobbyRepository = lobbyRepository) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel class, not a CreateLobbyViewModel")
        }
    }
}