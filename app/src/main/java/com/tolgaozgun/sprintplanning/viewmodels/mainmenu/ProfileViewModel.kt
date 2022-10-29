package com.tolgaozgun.sprintplanning.viewmodels.mainmenu

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.viewModelScope
import com.tolgaozgun.sprintplanning.data.model.User
import com.tolgaozgun.sprintplanning.repository.LobbyRepository
import com.tolgaozgun.sprintplanning.util.LocalUtil
import com.tolgaozgun.sprintplanning.viewmodels.TransactionViewModel
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val context: Context,
    private val lobbyRepository: LobbyRepository,
    fragmentManager: FragmentManager
)   : TransactionViewModel(fragmentManager = fragmentManager) {


    fun saveChanges(name: String?, avatar: String?): Boolean{
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("local_user", Context.MODE_PRIVATE)

        with(sharedPreferences.edit()){
            if(name != null){
                putString("name", name)
            }
            // TODO: Avatar
            apply()
        }

        viewModelScope.launch {
            lobbyRepository.addLocalUser(context)
        }

        return true
    }

    fun getUsername(context: Context): String{
        var user: User = LocalUtil.loadLocalUser(context)
        return user.name
    }

}