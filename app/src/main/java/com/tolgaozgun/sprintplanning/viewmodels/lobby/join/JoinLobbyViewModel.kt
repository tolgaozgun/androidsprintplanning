package com.tolgaozgun.sprintplanning.viewmodels.lobby.join

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.tolgaozgun.sprintplanning.repository.LobbyRepository
import com.tolgaozgun.sprintplanning.viewmodels.TransactionViewModel
import com.tolgaozgun.sprintplanning.views.lobby.LobbyFragment

class JoinLobbyViewModel(
    private var context: Context,
    fragmentManager: FragmentManager,
    lobbyRepository: LobbyRepository)
    : TransactionViewModel(fragmentManager = fragmentManager) {

    fun joinRoom(){
        // TODO: Add logic for joining a room


        replaceFragment(fragment = LobbyFragment(), shouldAddToBackStack = true)


    }

}