package com.tolgaozgun.sprintplanning.viewmodels.lobby.share

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ShareLobbyViewModelFactory(private val context: Context,
                                 private val fragmentManager: FragmentManager,
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ShareLobbyViewModel::class.java)){
            ShareLobbyViewModel(context, fragmentManager) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel class, not a ShareLobbyViewModel")
        }
    }
}