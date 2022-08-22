package com.tolgaozgun.sprintplanning.views.lobby

import androidx.fragment.app.FragmentManager
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.util.LobbyUtil
import com.tolgaozgun.sprintplanning.viewmodels.TransactionViewModel
import com.tolgaozgun.sprintplanning.views.lobby.share.ShareLobbyFragment
import com.tolgaozgun.sprintplanning.views.mainmenu.SettingsFragment

class LobbyViewModel(
    fragmentManager: FragmentManager
)   : TransactionViewModel(fragmentManager = fragmentManager)  {


    fun openSettings(currentLobby: Lobby){
        replaceFragment(
            fragment = SettingsFragment(),
            shouldAddToBackStack = true,
            arguments = LobbyUtil.createBundle(currentLobby)
        )
    }

    fun openShare(currentLobby: Lobby){
        replaceFragment(
            fragment = ShareLobbyFragment(),
            shouldAddToBackStack = true,
            arguments = LobbyUtil.createBundle(currentLobby)
        )
    }



}