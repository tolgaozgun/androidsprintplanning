package com.tolgaozgun.sprintplanning.viewmodels.mainmenu

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProfileViewModelFactory(private val fragmentManager: FragmentManager,
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProfileViewModel::class.java)){
            ProfileViewModel(fragmentManager = fragmentManager) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel class, not a ProfileViewModel")
        }
    }
}