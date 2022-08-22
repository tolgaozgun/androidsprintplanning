package com.tolgaozgun.sprintplanning.views.lobby

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class LobbyViewModelFactory(private val fragmentManager: FragmentManager,
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LobbyViewModel::class.java)){
            LobbyViewModel(fragmentManager) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel class, not a LobbyViewModel")
        }
    }
}