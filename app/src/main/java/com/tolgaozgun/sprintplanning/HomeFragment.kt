package com.tolgaozgun.sprintplanning

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.tolgaozgun.sprintplanning.databinding.FragmentHomeBinding
import com.tolgaozgun.sprintplanning.room.CreateRoomFragment
import com.tolgaozgun.sprintplanning.room.JoinRoomFragment


class HomeFragment : TransactionFragment() {

    private lateinit var binding : FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager

        binding.btnCreateRoom.setOnClickListener {
            replaceFragment(fragmentManager = fragmentManager, fragment = CreateRoomFragment(),
                shouldAddToBackStack = true)
        }
        binding.btnJoinRoom.setOnClickListener{
            replaceFragment(fragmentManager = fragmentManager, fragment = JoinRoomFragment(),
            shouldAddToBackStack = true)
        }
    }

}