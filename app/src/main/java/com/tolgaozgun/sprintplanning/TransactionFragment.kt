package com.tolgaozgun.sprintplanning

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

abstract class TransactionFragment : Fragment() {

    protected fun replaceFragment(fragmentManager: FragmentManager, fragment: Fragment,
                                  shouldAddToBackStack: Boolean = true){
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        if (shouldAddToBackStack)
            fragmentTransaction.addToBackStack(null)
            // Currently we will not pop more than one screen in the stack, so
            // putting null is the best case. Should be changed to a unique
            // string if popToBackStack(name, flags) is going to be used.
        fragmentTransaction.commit()
    }

    protected fun goBackFragment(fragmentManager: FragmentManager) : Boolean{
        return fragmentManager.popBackStackImmediate()
    }
}