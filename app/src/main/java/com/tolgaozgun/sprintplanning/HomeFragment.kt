package com.tolgaozgun.sprintplanning

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.tolgaozgun.sprintplanning.databinding.FragmentHomeBinding
import com.tolgaozgun.sprintplanning.room.CreateRoomFragment
import com.tolgaozgun.sprintplanning.room.JoinRoomFragment


class HomeFragment : Fragment() {

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
        binding.btnCreateRoom.setOnClickListener {
            replaceFragment(CreateRoomFragment())
        }
        binding.btnJoinRoom.setOnClickListener{
            replaceFragment(JoinRoomFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.addToBackStack("")
        fragmentTransaction.commit()
    }

}