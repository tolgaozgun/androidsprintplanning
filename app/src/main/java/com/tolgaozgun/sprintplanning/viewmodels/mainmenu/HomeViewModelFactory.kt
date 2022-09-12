package com.tolgaozgun.sprintplanning.viewmodels.mainmenu

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.auth.User
import com.tolgaozgun.sprintplanning.repository.LobbyRepository
import com.tolgaozgun.sprintplanning.repository.UserRepository

class HomeViewModelFactory(
    private val context: Context,
    private val fragmentManager: FragmentManager,
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            val userRepository: UserRepository = UserRepository.getInstance(context)
            val lobbyRepository: LobbyRepository = LobbyRepository.getInstance(context)
            HomeViewModel(context = context, userRepository, fragmentManager = fragmentManager,
                lobbyRepository = lobbyRepository) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel class, not a HomeViewModel")
        }
    }
}