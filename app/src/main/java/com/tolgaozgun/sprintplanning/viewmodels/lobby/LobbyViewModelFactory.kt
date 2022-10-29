package com.tolgaozgun.sprintplanning.viewmodels.lobby

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tolgaozgun.sprintplanning.repository.LobbyRepository


class LobbyViewModelFactory(private val context: Context,
                            private val fragmentManager: FragmentManager,
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LobbyViewModel::class.java)){
            val lobbyRepository: LobbyRepository = LobbyRepository.getInstance(context = context)
            LobbyViewModel(context, lobbyRepository, fragmentManager) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel class, not a LobbyViewModel")
        }
    }
}