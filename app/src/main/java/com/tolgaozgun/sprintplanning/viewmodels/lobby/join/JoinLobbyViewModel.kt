package com.tolgaozgun.sprintplanning.viewmodels.lobby.join

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.repository.LobbyRepository
import com.tolgaozgun.sprintplanning.util.LobbyUtil
import com.tolgaozgun.sprintplanning.viewmodels.TransactionViewModel
import com.tolgaozgun.sprintplanning.views.lobby.LobbyFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JoinLobbyViewModel(
    private var context: Context,
    fragmentManager: FragmentManager,
    private var lobbyRepository: LobbyRepository)
    : TransactionViewModel(fragmentManager = fragmentManager) {

    fun joinRoom(input: String, isJoining: MutableLiveData<Boolean>): Boolean{
        var code: String = input.trim().uppercase()
        var shouldJoin: Boolean = false

        viewModelScope.launch(Dispatchers.IO){
            var result: Lobby? = lobbyRepository.joinLobby(code, context)

            when(result){
                is Lobby ->{
                    isJoining.postValue(false)
                    replaceFragment(
                        fragment = LobbyFragment(),
                        shouldAddToBackStack = true,
                        arguments = LobbyUtil.createBundle(result)
                    )
//                    shouldContinue.postValue(false)
                }
                else ->{
                    isJoining.postValue(false)
//                    shouldContinue.postValue(true)
                }

            }

        }
        return shouldJoin

    }

}