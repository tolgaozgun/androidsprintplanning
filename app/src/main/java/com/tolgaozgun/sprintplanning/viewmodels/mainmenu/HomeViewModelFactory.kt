package com.tolgaozgun.sprintplanning.viewmodels.mainmenu

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HomeViewModelFactory(
    private val context: Context,
    private val fragmentManager: FragmentManager,
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(context = context, fragmentManager = fragmentManager) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel class, not a HomeViewModel")
        }
    }
}