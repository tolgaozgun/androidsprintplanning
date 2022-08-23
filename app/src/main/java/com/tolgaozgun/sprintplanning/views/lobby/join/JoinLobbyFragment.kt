package com.tolgaozgun.sprintplanning.views.lobby.join

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tolgaozgun.sprintplanning.databinding.FragmentJoinRoomBinding
import com.tolgaozgun.sprintplanning.viewmodels.lobby.join.JoinLobbyViewModel
import com.tolgaozgun.sprintplanning.viewmodels.lobby.join.JoinLobbyViewModelFactory

class JoinLobbyFragment : Fragment() {

    private lateinit var binding: FragmentJoinRoomBinding
    private lateinit var viewModel: JoinLobbyViewModel
    private lateinit var viewModelFactory: JoinLobbyViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        viewModelFactory = JoinLobbyViewModelFactory(context = requireContext(),
            fragmentManager = fragmentManager)
        viewModel = viewModelFactory.create(JoinLobbyViewModel::class.java)
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


        binding.imgBack.setOnClickListener{
            viewModel.goBackFragment()
        }

        binding.btnJoinRoom.setOnClickListener {
            viewModel.joinRoom()
        }
    }

}