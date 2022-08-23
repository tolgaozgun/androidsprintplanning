package com.tolgaozgun.sprintplanning.viewmodels.mainmenu

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentManager
import com.tolgaozgun.sprintplanning.viewmodels.TransactionViewModel

class HomeViewModel(
    private val context: Context,
    fragmentManager: FragmentManager
)   : TransactionViewModel(fragmentManager = fragmentManager) {

    fun getUsername(): String?{
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("local_user", Context.MODE_PRIVATE)

        return sharedPreferences.getString("name", null)
    }


}