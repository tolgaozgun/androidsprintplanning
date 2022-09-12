package com.tolgaozgun.sprintplanning.views.mainmenu

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tolgaozgun.sprintplanning.R
import com.tolgaozgun.sprintplanning.data.model.Lobby
import com.tolgaozgun.sprintplanning.data.model.User
import com.tolgaozgun.sprintplanning.databinding.FragmentHomeBinding
import com.tolgaozgun.sprintplanning.viewmodels.mainmenu.HomeViewModel
import com.tolgaozgun.sprintplanning.viewmodels.mainmenu.HomeViewModelFactory
import com.tolgaozgun.sprintplanning.views.lobby.create.CreateLobbyFragment
import com.tolgaozgun.sprintplanning.views.lobby.join.JoinLobbyFragment


class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var viewModelFactory: HomeViewModelFactory
    var isCheckingLobby: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var isPreviousLobbyChecked: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    private var progressDialog: ProgressDialog? = null


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
        viewModelFactory = HomeViewModelFactory(fragmentManager = fragmentManager,
            context = requireContext())
        viewModel = viewModelFactory.create(HomeViewModel::class.java)


        val userName: String? = viewModel.getUsername()

        val text: String = getString(R.string.hello_name, userName)
        setupObservers()
        viewModel.loadUser()
        if(!isPreviousLobbyChecked.value!!){
            isCheckingLobby.postValue(true)
            showProgressDialog()
            viewModel.checkLobby(requireContext(), isCheckingLobby)
            isPreviousLobbyChecked.postValue(true)
        }
    }

    private fun setupObservers(){
        viewModel.user.observe(viewLifecycleOwner, Observer<User> { user ->
            val name: String = if(user.name.length > 16){
                user.name.take(12) + "..."
            }else{
                user.name
            }
            binding.txtTitle.text = name
            binding.imgAvatar.setImageBitmap(user.avatar)
        })

        isCheckingLobby.observe(viewLifecycleOwner, Observer<Boolean>{ isCheckingLobby ->
            if(progressDialog != null && progressDialog!!.isShowing && !isCheckingLobby){
                progressDialog!!.dismiss()
            }

        })
    }

    private fun showProgressDialog(){
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Trying to join previous lobby...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
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