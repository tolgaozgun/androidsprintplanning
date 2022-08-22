package com.tolgaozgun.sprintplanning.views.lobby.share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.databinding.FragmentShareRoomBinding
import com.tolgaozgun.sprintplanning.util.LobbyUtil
import com.tolgaozgun.sprintplanning.viewmodels.lobby.share.ShareLobbyViewModel
import com.tolgaozgun.sprintplanning.viewmodels.lobby.share.ShareLobbyViewModelFactory

class ShareLobbyFragment : Fragment() {

    private lateinit var binding: FragmentShareRoomBinding
    private lateinit var viewModel: ShareLobbyViewModel
    private lateinit var viewModelFactory: ShareLobbyViewModelFactory
    private lateinit var lobby: Lobby

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        viewModelFactory = ShareLobbyViewModelFactory(context = requireContext(),
            fragmentManager = fragmentManager)
        viewModel = viewModelFactory.create(ShareLobbyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShareRoomBinding.inflate(inflater, container, false)
        val bundle: Bundle? = arguments
        if(bundle != null){
            lobby = LobbyUtil.fromBundle(bundle)
            binding.txtRoomId.text = lobby.code



        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgBack.setOnClickListener {
            viewModel.goBackFragment()
        }
    }


}