package com.tolgaozgun.sprintplanning.room

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.tolgaozgun.sprintplanning.HomeFragment
import com.tolgaozgun.sprintplanning.R
import com.tolgaozgun.sprintplanning.TransactionFragment
import com.tolgaozgun.sprintplanning.databinding.FragmentRoomBinding

class RoomFragment : TransactionFragment() {

    private lateinit var binding: FragmentRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager

        // TODO: Add a menu to confirm leave
        binding.imgLeave.setOnClickListener{
            replaceFragment(fragmentManager = fragmentManager, fragment = HomeFragment(),
                shouldAddToBackStack = false)
        }

        // TODO: Check user permissions to display room settings
        // Only admins should be able to change room settings
        // (Should users be able to display them?)
        binding.linearLayoutRoomHeader.setOnClickListener {
            replaceFragment(fragmentManager = fragmentManager, fragment = RoomSettingsFragment(),
                shouldAddToBackStack = true)
        }

        // TODO: Add the room ID and QR code to the share screen
        binding.imgRoomShare.setOnClickListener{
            replaceFragment(fragmentManager = fragmentManager, fragment = ShareRoomFragment(),
                shouldAddToBackStack = true)
        }
    }
}