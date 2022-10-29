package com.tolgaozgun.sprintplanning.viewmodels.mainmenu

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tolgaozgun.sprintplanning.repository.LobbyRepository

class MainViewModelFactory(
    private val context: Context,
    private val fragmentManager: FragmentManager,
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            val lobbyRepository: LobbyRepository = LobbyRepository.getInstance(context = context)
            MainViewModel(fragmentManager = fragmentManager, lobbyRepository) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel class, not a MainViewModel")
        }
    }
}