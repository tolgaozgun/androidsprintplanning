package com.tolgaozgun.sprintplanning.views.lobby

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.data.views.UsersViewAdapter
import com.tolgaozgun.sprintplanning.data.views.VoteCardAdapter
import com.tolgaozgun.sprintplanning.databinding.FragmentRoomBinding
import com.tolgaozgun.sprintplanning.util.LobbyUtil
import com.tolgaozgun.sprintplanning.views.mainmenu.HomeFragment

class LobbyFragment : Fragment() {

    private lateinit var voteCardAdapter: VoteCardAdapter
    private lateinit var usersViewAdapter: UsersViewAdapter
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

        setupView()
        setupVoteCard()
        setupUsersView()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        viewModelFactory = LobbyViewModelFactory(fragmentManager = fragmentManager,
            context = requireContext())
        viewModel = viewModelFactory.create(LobbyViewModel::class.java)

    }

    private fun setupView(){
        val bundle: Bundle? = arguments
        if(bundle != null){
            lobby = LobbyUtil.fromBundle(bundle)
            binding.txtTitle.text = lobby.name
            binding.txtSubtitle.text = lobby.status.toString()
        }
    }

    private fun setupVoteCard(){
        val listener =
            VoteCardAdapter.VoteCardClickListener { item ->
                Toast.makeText(requireContext(), "Item Clicked $item", Toast.LENGTH_LONG).show()
            }

        voteCardAdapter = VoteCardAdapter(requireContext(), listener)
        binding.linearLayoutVoteContent.adapter = voteCardAdapter
        binding.linearLayoutVoteContent.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupUsersView(){
        val listener =
            UsersViewAdapter.UsersViewClickListener { user ->
                Toast.makeText(requireContext(), "User Clicked ${user.name}", Toast.LENGTH_SHORT).show()
            }

        usersViewAdapter = UsersViewAdapter(requireContext(), lobby.users, listener)
        binding.recyclerViewUsers.adapter = usersViewAdapter
        binding.recyclerViewUsers.layoutManager = GridLayoutManager(requireContext(), 3)

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
        binding.imgSettings.setOnClickListener{
            viewModel.openSettings(lobby)
        }

        binding.imgRoomShare.setOnClickListener{
            viewModel.openShare(lobby)
        }
    }
}