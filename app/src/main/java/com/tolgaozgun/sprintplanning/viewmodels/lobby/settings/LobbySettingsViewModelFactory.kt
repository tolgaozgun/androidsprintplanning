package com.tolgaozgun.sprintplanning.viewmodels.lobby.settings

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tolgaozgun.sprintplanning.repository.LobbyRepository
import com.tolgaozgun.sprintplanning.viewmodels.lobby.join.JoinLobbyViewModel
import com.tolgaozgun.sprintplanning.viewmodels.mainmenu.SettingsViewModel


class LobbySettingsViewModelFactory(private val context: Context,
                                private val fragmentManager: FragmentManager,
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LobbySettingsViewModel::class.java)){
//            val lobbyRepository: LobbyRepository = LobbyRepository.getInstance(context = context)
            LobbySettingsViewModel(context, fragmentManager) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel class, not a LobbySettingsViewModel")
        }
    }
}