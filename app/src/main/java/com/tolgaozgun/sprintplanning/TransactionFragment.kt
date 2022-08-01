package com.tolgaozgun.sprintplanning

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

abstract class TransactionFragment : Fragment() {

    protected fun replaceFragment(fragmentManager: FragmentManager, fragment: Fragment, shouldAddToBackStack: Boolean = true){
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        if (shouldAddToBackStack)
            fragmentTransaction.addToBackStack("")
        fragmentTransaction.commit()
    }

    protected fun goBackFragment(fragmentManager: FragmentManager) : Boolean{
        return fragmentManager.popBackStackImmediate()
    }
}