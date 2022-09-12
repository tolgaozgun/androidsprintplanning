package com.tolgaozgun.sprintplanning.views.lobby

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tolgaozgun.sprintplanning.R
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.data.views.UsersViewAdapter
import com.tolgaozgun.sprintplanning.data.views.VoteCardAdapter
import com.tolgaozgun.sprintplanning.databinding.FragmentRoomBinding
import com.tolgaozgun.sprintplanning.util.LobbyUtil
import com.tolgaozgun.sprintplanning.viewmodels.lobby.LobbyViewModel
import com.tolgaozgun.sprintplanning.viewmodels.lobby.LobbyViewModelFactory
import com.tolgaozgun.sprintplanning.views.mainmenu.HomeFragment

class LobbyFragment : Fragment() {

    private lateinit var voteCardAdapter: VoteCardAdapter
    private lateinit var usersViewAdapter: UsersViewAdapter
    private lateinit var binding: FragmentRoomBinding
    private lateinit var viewModel: LobbyViewModel
    private lateinit var viewModelFactory: LobbyViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        LobbyUtil.isInLobby = true
        binding = FragmentRoomBinding.inflate(inflater, container, false)

        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        viewModelFactory = LobbyViewModelFactory(fragmentManager = fragmentManager,
            context = requireContext())
        viewModel = viewModelFactory.create(LobbyViewModel::class.java)

        setupView()
        setupVoteCard()
        setupUsersView()
        setupObserver()


        return binding.root
    }

    private fun setupObserver(){
        viewModel.lobby.observe(viewLifecycleOwner, Observer<Lobby> { lobby ->
            Log.d("LOBBY_OBSERVER", "Observing lobby livedata.. (UPDATE)")
            Log.d("LOBBY_OBSERVER", "Observing lobby livedata.. userCOunt: ${lobby.users.count()}")

            binding.txtTitle.text = lobby.code
            binding.txtSubtitle.text = lobby.status.value
            usersViewAdapter.updateList(lobby.users, lobby.showResults)

            var voted: Int = 0
            for(user in lobby.users){
                if(user.hasVoted)
                    voted++
            }

            if(lobby.showResults){
                binding.btnShowResults.setText(R.string.reset_vote)
            }else{
                binding.btnShowResults.setText(R.string.show_results)
            }

            binding.txtVoteStatus.text = getString(R.string.vote_count, voted, lobby.users.count())
            LobbyUtil.lobby?.postValue(lobby)
        })
    }


    private fun setupView(){
        val bundle: Bundle? = arguments
        if(bundle != null){
            viewModel.lobby.value = LobbyUtil.fromBundle(bundle)
            binding.txtTitle.text = viewModel.lobby.value!!.name
            binding.txtSubtitle.text = viewModel.lobby.value!!.status.toString()
        }
        viewModel.subscribeToLobby()
    }

    private fun setupVoteCard(){
        val listener =
            VoteCardAdapter.VoteCardClickListener { item ->
                viewModel.vote(item)
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

        usersViewAdapter = UsersViewAdapter(requireContext(), viewModel.lobby.value!!.users, viewModel.lobby.value!!.showResults, listener)
        binding.recyclerViewUsers.adapter = usersViewAdapter
        binding.recyclerViewUsers.layoutManager = GridLayoutManager(requireContext(), 3)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Add a menu to confirm leave
        binding.imgLeave.setOnClickListener{
            viewModel.replaceFragment(fragment = HomeFragment(),
                shouldAddToBackStack = false)
        }

        // TODO: Check user permissions to display room settings
        // Only admins should be able to change room settings
        // (Should users be able to display them?)
//        binding.imgSettings.setOnClickListener{
//            viewModel.openSettings()
//        }

        binding.imgRoomShare.setOnClickListener{
            viewModel.openShare()
        }

        binding.btnShowResults.setOnClickListener {
            var result: Boolean? = viewModel.showResults()
            if(result != null){
                if(result){
                    binding.btnShowResults.setText(R.string.reset_vote)
                }else{
                    binding.btnShowResults.setText(R.string.show_results)
                }
            }
        }

        binding.imgLeave.setOnClickListener {
            viewModel.leaveLobby()
        }
    }
}