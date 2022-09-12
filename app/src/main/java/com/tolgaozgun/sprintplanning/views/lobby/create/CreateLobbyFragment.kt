package com.tolgaozgun.sprintplanning.views.lobby.create

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tolgaozgun.sprintplanning.databinding.FragmentCreateRoomBinding
import com.tolgaozgun.sprintplanning.viewmodels.lobby.create.CreateLobbyViewModel
import com.tolgaozgun.sprintplanning.viewmodels.lobby.create.CreateLobbyViewModelFactory

class CreateLobbyFragment : Fragment() {

    private lateinit var binding: FragmentCreateRoomBinding
    private lateinit var viewModel: CreateLobbyViewModel
    private lateinit var viewModelFactory: CreateLobbyViewModelFactory
    var progressDialog: ProgressDialog? = null
    var isCreating: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

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

        isCreating.observe(viewLifecycleOwner, Observer<Boolean>{ isCreating ->
            if(progressDialog != null && progressDialog!!.isShowing && !isCreating){
                progressDialog!!.dismiss()
            }
        })
    }

    private fun showProgressDialog(){
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Creating lobby..")
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }

    override fun onDestroy() {
        if(progressDialog != null && progressDialog!!.isShowing){
            progressDialog!!.dismiss()
        }
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.slUserLimit.addOnChangeListener { slider, value, fromUser ->
//            viewModel.setUserLimit(value.toInt())
//        }
//
//        binding.swAskToJoin.setOnCheckedChangeListener { compoundButton, value ->
//            viewModel.setAskToJoin(value)
//        }

        binding.imgBack.setOnClickListener{
            viewModel.goBackFragment()
        }

        binding.btnCreateRoom.setOnClickListener{
            isCreating.postValue(true)
            showProgressDialog()
            viewModel.createLobby(isCreating)
        }
    }

}