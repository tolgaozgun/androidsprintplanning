package com.tolgaozgun.sprintplanning.room

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.tolgaozgun.sprintplanning.R
import com.tolgaozgun.sprintplanning.TransactionFragment
import com.tolgaozgun.sprintplanning.databinding.FragmentJoinRoomBinding

class JoinRoomFragment : TransactionFragment() {

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

        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        binding.imgBack.setOnClickListener{
            goBackFragment(fragmentManager = fragmentManager)
        }

        // TODO: Add logic for connecting to a room
        binding.btnJoinRoom.setOnClickListener {
            replaceFragment(fragmentManager = fragmentManager, fragment = RoomFragment(),
                 shouldAddToBackStack = true)
        }
    }

}