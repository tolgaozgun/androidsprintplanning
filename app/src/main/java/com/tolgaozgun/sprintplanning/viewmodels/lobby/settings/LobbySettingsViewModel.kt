package com.tolgaozgun.sprintplanning.viewmodels.lobby.settings

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.tolgaozgun.sprintplanning.viewmodels.TransactionViewModel

class LobbySettingsViewModel(
    private var context: Context,
    fragmentManager: FragmentManager
) : TransactionViewModel(fragmentManager = fragmentManager)  {

}