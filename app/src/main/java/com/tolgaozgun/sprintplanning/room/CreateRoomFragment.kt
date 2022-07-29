package com.tolgaozgun.sprintplanning.room

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.tolgaozgun.sprintplanning.R
import com.tolgaozgun.sprintplanning.databinding.FragmentCreateRoomBinding

class CreateRoomFragment : Fragment() {

    private lateinit var binding: FragmentCreateRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgBackCreateRoom.setOnClickListener{
            goBackFragment()
        }

        // TODO: Add logic for retrieving the settings and creating a room in the network
        binding.btnCreateRoomFinal.setOnClickListener{
            replaceFragment(RoomFragment())
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
    }

}