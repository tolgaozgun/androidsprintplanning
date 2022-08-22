package com.tolgaozgun.sprintplanning.views.mainmenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tolgaozgun.sprintplanning.databinding.FragmentHomeBinding
import com.tolgaozgun.sprintplanning.viewmodels.mainmenu.HomeViewModel
import com.tolgaozgun.sprintplanning.viewmodels.mainmenu.HomeViewModelFactory
import com.tolgaozgun.sprintplanning.views.lobby.create.CreateLobbyFragment
import com.tolgaozgun.sprintplanning.views.lobby.join.JoinLobbyFragment


class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var viewModelFactory: HomeViewModelFactory

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

    override fun onStart() {
        super.onStart()
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        viewModelFactory = HomeViewModelFactory(fragmentManager = fragmentManager)
        viewModel = viewModelFactory.create(HomeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreateRoom.setOnClickListener {
            viewModel.replaceFragment(fragment = CreateLobbyFragment(),
                shouldAddToBackStack = true)
        }
        binding.btnJoinRoom.setOnClickListener{
            viewModel.replaceFragment(fragment = JoinLobbyFragment(),
            shouldAddToBackStack = true)
        }
    }

}