package com.tolgaozgun.sprintplanning.views.lobby

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.repository.LobbyRepository
import com.tolgaozgun.sprintplanning.util.LobbyUtil
import com.tolgaozgun.sprintplanning.util.LocalUtil
import com.tolgaozgun.sprintplanning.viewmodels.TransactionViewModel
import com.tolgaozgun.sprintplanning.views.lobby.settings.LobbySettingsFragment
import com.tolgaozgun.sprintplanning.views.lobby.share.ShareLobbyFragment
import com.tolgaozgun.sprintplanning.views.mainmenu.HomeFragment
import com.tolgaozgun.sprintplanning.views.mainmenu.SettingsFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class LobbyViewModel(
    val context: Context,
    var lobbyRepository: LobbyRepository,
    fragmentManager: FragmentManager
)   : TransactionViewModel(fragmentManager = fragmentManager)  {


    var lobby: MutableLiveData<Lobby> = MutableLiveData<Lobby>()

    fun leaveLobby(){
        viewModelScope.launch {
            lobbyRepository.leaveLobby(context, lobby.value!!)
            LocalUtil.removeLobbyFromLocal(context)
            replaceFragment(
                fragment = HomeFragment(),
                shouldAddToBackStack = false
            )
        }

    }

    fun openSettings(){
        replaceFragment(
            fragment = LobbySettingsFragment(),
            shouldAddToBackStack = true,
            arguments = LobbyUtil.createBundle(lobby.value!!)
        )
    }

    fun updateLobby(snapshot: DocumentSnapshot){
        viewModelScope.launch {
            lobby.value = Lobby.loadSnapshot(context, snapshot)
        }
    }


    fun subscribeToLobby(){
        var instance: LobbyViewModel = this
        viewModelScope.launch(Dispatchers.IO){
            lobbyRepository.subscribeLobby(context, lobby, instance)
        }
    }

    fun openShare(){
        replaceFragment(
            fragment = ShareLobbyFragment(),
            shouldAddToBackStack = true,
            arguments = LobbyUtil.createBundle(lobby.value!!)
        )
    }

    fun vote(value: Int){
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("local_user", Context.MODE_PRIVATE)



        viewModelScope.launch(Dispatchers.IO){
            val result: Boolean = lobbyRepository.voteLobby(context, value)
            withContext(Dispatchers.Main){
                when(result){
                    true ->
                        Toast.makeText(context, "Vote registered $value", Toast.LENGTH_LONG).show()
                    false ->
                        Toast.makeText(context, "Error while voting", Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    fun showResults(): Boolean?{
        var isError: Boolean = false
        val latestState: Boolean = !lobby.value!!.showResults
        viewModelScope.launch(Dispatchers.IO){
            val result: Boolean = lobbyRepository.showResults(context, lobby.value!!, latestState)
            withContext(Dispatchers.Main){
                when(result){
                    true -> {
                        if(latestState){
                            Toast.makeText(context, "Showing results", Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(context, "Resetting votes", Toast.LENGTH_LONG).show()
                        }

                        // TODO: Check this
                        lobby.value!!.showResults = latestState
//                        val oldLobby: Lobby = lobby.value!!
//                        oldLobby.showResults = latestState
//                        lobby.value = oldLobby
                    }
                    false -> {
                        Toast.makeText(context, "Error while loading votes", Toast.LENGTH_LONG).show()
                        isError = true
                    }
                }
            }
        }
        if(isError)
            return null
        return latestState
    }





}