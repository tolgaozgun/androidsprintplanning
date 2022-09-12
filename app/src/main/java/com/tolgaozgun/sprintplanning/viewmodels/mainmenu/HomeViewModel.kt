package com.tolgaozgun.sprintplanning.viewmodels.mainmenu

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.data.model.User
import com.tolgaozgun.sprintplanning.repository.LobbyRepository
import com.tolgaozgun.sprintplanning.repository.UserRepository
import com.tolgaozgun.sprintplanning.util.LobbyUtil
import com.tolgaozgun.sprintplanning.util.LocalUtil
import com.tolgaozgun.sprintplanning.viewmodels.TransactionViewModel
import com.tolgaozgun.sprintplanning.views.lobby.LobbyFragment
import kotlinx.coroutines.launch

class HomeViewModel(
    private val context: Context,
    private val userRepository: UserRepository,
    private val lobbyRepository: LobbyRepository,
    fragmentManager: FragmentManager
)   : TransactionViewModel(fragmentManager = fragmentManager) {

    var user: MutableLiveData<User> = MutableLiveData<User>()

    fun getUsername(): String?{
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("local_user", Context.MODE_PRIVATE)

        return sharedPreferences.getString("name", null)
    }

    fun loadUser() {
        viewModelScope.launch {
            user.postValue(LocalUtil.loadLocalUserWithAvatar(context, userRepository, true))
        }
    }
    fun checkLobby(context: Context, isCheckingLobby: MutableLiveData<Boolean>){
        Log.d("CHECK_LOBBY", "Lobby checking..")
        if(LocalUtil.isJoinedLobby(context)){
            Log.d("CHECK_LOBBY", "Lobby is joined..")
            val code: String = LocalUtil.getLocalLobbyCode(context)!!
            Log.d("CHECK_LOBBY", "Lobby code: $code")
            viewModelScope.launch {
                when(val lobby: Lobby? = lobbyRepository.joinLobby(code, context, true)){
                    is Lobby -> {
                        isCheckingLobby.postValue(false)
                        try{
                            replaceFragment(
                                fragment = LobbyFragment(),
                                shouldAddToBackStack = true,
                                arguments = LobbyUtil.createBundle(lobby)
                            )
                            Toast.makeText(context, "You were joined back to your previous lobby", Toast.LENGTH_SHORT).show()
                        }catch(e: Exception){
                            Toast.makeText(context, "Error on joining on your previous lobby", Toast.LENGTH_SHORT).show()
                        }

                    }
                    else -> {
                        isCheckingLobby.postValue(false)
                        Toast.makeText(context, "Your previous lobby was disbanded, cannot join.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        isCheckingLobby.postValue(false)

    }

}