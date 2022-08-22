package com.tolgaozgun.sprintplanning.views.lobby.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tolgaozgun.sprintplanning.databinding.FragmentRoomSettingsBinding
import com.tolgaozgun.sprintplanning.viewmodels.lobby.settings.LobbySettingsViewModel
import com.tolgaozgun.sprintplanning.viewmodels.lobby.settings.LobbySettingsViewModelFactory

class LobbySettingsFragment : Fragment() {

    private lateinit var binding: FragmentRoomSettingsBinding
    private lateinit var viewModel: LobbySettingsViewModel
    private lateinit var viewModelFactory: LobbySettingsViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        viewModelFactory = LobbySettingsViewModelFactory(context = requireContext(),
            fragmentManager = fragmentManager)
        viewModel = viewModelFactory.create(LobbySettingsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoomSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgBack.setOnClickListener{
            viewModel.goBackFragment()
        }
    }


}