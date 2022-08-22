package com.tolgaozgun.sprintplanning.viewmodels.lobby.create

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.viewModelScope
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.data.model.LobbyState
import com.tolgaozgun.sprintplanning.repository.LobbyRepository
import com.tolgaozgun.sprintplanning.util.LobbyUtil
import com.tolgaozgun.sprintplanning.viewmodels.TransactionViewModel
import com.tolgaozgun.sprintplanning.views.lobby.LobbyFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class CreateLobbyViewModel(
    fragmentManager: FragmentManager,
    private var context: Context,
    private var mUserLimit: Int = 12,
    private var mAskToJoin: Boolean = false,
    private var lobbyRepository: LobbyRepository,
) : TransactionViewModel(fragmentManager = fragmentManager) {

    fun getUserLimit() : Int {
        return mUserLimit
    }

    fun setUserLimit(userLimit: Int){
        mUserLimit = userLimit
    }

    fun setAskToJoin(askToJoin: Boolean){
        mAskToJoin = askToJoin
    }


    fun createLobby() {
        val timeNow = System.currentTimeMillis()

        val pendingLobby: Lobby = Lobby(

            userLimit = mUserLimit,
            askToJoin = mAskToJoin,
            id = UUID.randomUUID(),
            name = "Room",
            status = LobbyState.WAITING,
            timeCreated = timeNow,
            timeUpdated = timeNow,
            code = LobbyUtil.createCode()
//            users = listOf<User>(),
        )



        viewModelScope.launch(Dispatchers.IO){
            val result: Lobby = lobbyRepository.createLobby(pendingLobby)
            when(result){
                is Lobby ->
                    replaceFragment(
                        fragment = LobbyFragment(),
                        shouldAddToBackStack = true,
                        arguments = LobbyUtil.createBundle(result)
                    )
                else ->
                    // Error
                    Toast.makeText(context, "Error occurred during db connection", Toast.LENGTH_LONG).show()
            }

        }

    }




}