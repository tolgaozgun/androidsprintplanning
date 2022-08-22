package com.tolgaozgun.sprintplanning.views.lobby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.data.model.LobbyState
import com.tolgaozgun.sprintplanning.databinding.FragmentRoomBinding
import com.tolgaozgun.sprintplanning.util.LobbyUtil
import com.tolgaozgun.sprintplanning.views.lobby.settings.LobbySettingsFragment
import com.tolgaozgun.sprintplanning.views.lobby.share.ShareLobbyFragment
import com.tolgaozgun.sprintplanning.views.mainmenu.HomeFragment
import java.util.*

class LobbyFragment : Fragment() {

    private lateinit var binding: FragmentRoomBinding
    private lateinit var viewModel: LobbyViewModel
    private lateinit var viewModelFactory: LobbyViewModelFactory
    private lateinit var lobby: Lobby

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoomBinding.inflate(inflater, container, false)

        val bundle: Bundle? = arguments
        if(bundle != null){
            lobby = LobbyUtil.fromBundle(bundle)
            binding.txtTitle.text = lobby.name
            binding.txtSubtitle.text = lobby.status.toString()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        viewModelFactory = LobbyViewModelFactory(fragmentManager = fragmentManager)
        viewModel = viewModelFactory.create(LobbyViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager

        // TODO: Add a menu to confirm leave
        binding.imgLeave.setOnClickListener{
            viewModel.replaceFragment(fragment = HomeFragment(),
                shouldAddToBackStack = false)
        }

        // TODO: Check user permissions to display room settings
        // Only admins should be able to change room settings
        // (Should users be able to display them?)
        binding.linearLayoutRoomHeader.setOnClickListener {
            viewModel.replaceFragment(fragment = LobbySettingsFragment(),
                shouldAddToBackStack = true)
        }

        // TODO: Add the room ID and QR code to the share screen
        binding.imgRoomShare.setOnClickListener{
            viewModel.replaceFragment(fragment = ShareLobbyFragment(),
                shouldAddToBackStack = true)
        }

        binding.imgSettings.setOnClickListener{
            viewModel.openSettings(lobby)
        }
    }
}