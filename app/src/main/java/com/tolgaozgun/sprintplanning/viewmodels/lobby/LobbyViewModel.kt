package com.tolgaozgun.sprintplanning.viewmodels.lobby

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.data.model.User
import com.tolgaozgun.sprintplanning.repository.LobbyRepository
import com.tolgaozgun.sprintplanning.util.LobbyUtil
import com.tolgaozgun.sprintplanning.util.LocalUtil
import com.tolgaozgun.sprintplanning.viewmodels.TransactionViewModel
import com.tolgaozgun.sprintplanning.views.lobby.settings.LobbySettingsFragment
import com.tolgaozgun.sprintplanning.views.lobby.share.ShareLobbyFragment
import com.tolgaozgun.sprintplanning.views.mainmenu.HomeFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class LobbyViewModel(
    val context: Context,
    var lobbyRepository: LobbyRepository,
    fragmentManager: FragmentManager
)   : TransactionViewModel(fragmentManager = fragmentManager)  {

    private lateinit var lobbySubscription: ListenerRegistration
    var lobby: MutableLiveData<Lobby> = MutableLiveData<Lobby>()
    var users: MutableLiveData<MutableMap<UUID, ListenerRegistration>> =
        MutableLiveData<MutableMap<UUID, ListenerRegistration>>()

    fun leaveLobby(){
        viewModelScope.launch {
            lobbySubscription.remove()
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
        var instance: LobbyViewModel = this
        viewModelScope.launch {
            lobby.value = Lobby.loadSnapshot(context, snapshot)
            lobbyRepository.subscribeUsers(context, lobby.value!!, users, instance)
        }
    }

    fun reloadLobby(){
        viewModelScope.launch {
            lobby.postValue(lobbyRepository.getLobby(context, lobby.value!!.code))
        }
    }


    fun subscribeToLobby(){
        var instance: LobbyViewModel = this
        viewModelScope.launch(Dispatchers.IO){
            lobbySubscription = lobbyRepository.subscribeLobby(context, lobby, instance)
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