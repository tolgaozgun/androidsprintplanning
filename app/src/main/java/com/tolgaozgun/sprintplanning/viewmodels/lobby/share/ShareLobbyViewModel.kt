package com.tolgaozgun.sprintplanning.viewmodels.lobby.share

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.tolgaozgun.sprintplanning.viewmodels.TransactionViewModel

class ShareLobbyViewModel(
    private var context: Context,
    fragmentManager: FragmentManager
) : TransactionViewModel(fragmentManager = fragmentManager)  {

}