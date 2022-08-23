package com.tolgaozgun.sprintplanning.views.lobby

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.viewModelScope
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.repository.LobbyRepository
import com.tolgaozgun.sprintplanning.util.LobbyUtil
import com.tolgaozgun.sprintplanning.viewmodels.TransactionViewModel
import com.tolgaozgun.sprintplanning.views.lobby.settings.LobbySettingsFragment
import com.tolgaozgun.sprintplanning.views.lobby.share.ShareLobbyFragment
import com.tolgaozgun.sprintplanning.views.mainmenu.SettingsFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class LobbyViewModel(
    val context: Context,
    private var lobbyRepository: LobbyRepository,
    fragmentManager: FragmentManager
)   : TransactionViewModel(fragmentManager = fragmentManager)  {


    fun openSettings(currentLobby: Lobby){
        replaceFragment(
            fragment = LobbySettingsFragment(),
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

    fun vote(value: Int){
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("local_user", Context.MODE_PRIVATE)

        val idString: String = ""
        sharedPreferences.getString("id", idString)


        viewModelScope.launch(Dispatchers.IO){
            val result: Boolean = lobbyRepository.voteLobby(value, idString)
            when(result){
                true ->
                    Toast.makeText(context, "Vote registered $value", Toast.LENGTH_LONG).show()
                false ->
                    // Error
                    Toast.makeText(context, "Error while voting", Toast.LENGTH_LONG).show()
            }

        }
    }





}