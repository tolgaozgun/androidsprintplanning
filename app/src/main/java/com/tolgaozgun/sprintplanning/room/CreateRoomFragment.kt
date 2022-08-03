package com.tolgaozgun.sprintplanning.room

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.tolgaozgun.sprintplanning.R
import com.tolgaozgun.sprintplanning.TransactionFragment
import com.tolgaozgun.sprintplanning.databinding.FragmentCreateRoomBinding

class CreateRoomFragment : TransactionFragment() {

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

        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        binding.imgBack.setOnClickListener{
            goBackFragment(fragmentManager)
        }

        // TODO: Add logic for retrieving the settings and creating a room in the network
        binding.btnCreateRoom.setOnClickListener{
            replaceFragment(fragmentManager = fragmentManager, fragment = RoomFragment(),
                shouldAddToBackStack = true)
        }
    }

}