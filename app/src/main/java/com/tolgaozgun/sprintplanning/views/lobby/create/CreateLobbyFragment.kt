package com.tolgaozgun.sprintplanning.views.lobby.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tolgaozgun.sprintplanning.databinding.FragmentCreateRoomBinding
import com.tolgaozgun.sprintplanning.viewmodels.lobby.create.CreateLobbyViewModel
import com.tolgaozgun.sprintplanning.viewmodels.lobby.create.CreateLobbyViewModelFactory

class CreateLobbyFragment : Fragment() {

    private lateinit var binding: FragmentCreateRoomBinding
    private lateinit var viewModel: CreateLobbyViewModel
    private lateinit var viewModelFactory: CreateLobbyViewModelFactory

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

    override fun onStart() {
        super.onStart()
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        viewModelFactory = CreateLobbyViewModelFactory(context = requireContext(),
            fragmentManager = fragmentManager)
        viewModel = viewModelFactory.create(CreateLobbyViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgBack.setOnClickListener{
            viewModel.goBackFragment()
        }

        binding.btnCreateRoom.setOnClickListener{
            viewModel.createLobby()

        }
    }

}