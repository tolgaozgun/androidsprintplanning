package com.tolgaozgun.sprintplanning.room

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.tolgaozgun.sprintplanning.R
import com.tolgaozgun.sprintplanning.databinding.FragmentJoinRoomBinding

class JoinRoomFragment : Fragment() {

    private lateinit var binding: FragmentJoinRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJoinRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgBackJoinRoom.setOnClickListener{
            Toast.makeText(context, "Go back clicked", Toast.LENGTH_LONG).show()
            goBackFragment()
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.addToBackStack("")
        fragmentTransaction.commit()
    }

    private fun goBackFragment(){
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val result: Boolean = fragmentManager.popBackStackImmediate()
        Log.d("SPRINT_PLANNING", result.toString())
    }
}