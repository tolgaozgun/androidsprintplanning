package com.tolgaozgun.sprintplanning.viewmodels

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import com.tolgaozgun.sprintplanning.R
import com.tolgaozgun.sprintplanning.util.LobbyUtil
import com.tolgaozgun.sprintplanning.views.lobby.LobbyFragment

abstract class TransactionViewModel(
    protected open var fragmentManager: FragmentManager,
) : ViewModel() {

    protected var isInLobby: Boolean = false


    fun replaceFragment(fragment: Fragment,
                                  shouldAddToBackStack: Boolean = true,
                                  arguments: Bundle? = null,
                                  uniqueName: String? = null
    ){
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        arguments?.let{ args ->
            fragment.arguments = args
        }

        fragmentTransaction.replace(R.id.frame_layout, fragment)
        if (shouldAddToBackStack)
            fragmentTransaction.addToBackStack(uniqueName)
            // Currently we will not pop more than one screen in the stack, so
            // putting null is the best case. Should be changed to a unique
            // string if popToBackStack(name, flags) is going to be used.
        fragmentTransaction.commit()
    }

    fun popToLobby(){
        var bundle: Bundle = LobbyUtil.createBundle(LobbyUtil.lobby?.value!!)
        replaceFragment(
            fragment = LobbyFragment(),
            shouldAddToBackStack = true,
            arguments = bundle
        )
    }


    open fun goBackFragment() : Boolean{
        return fragmentManager.popBackStackImmediate()
    }
}