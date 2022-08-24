package com.tolgaozgun.sprintplanning.views.lobby.join

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.tolgaozgun.sprintplanning.R
import com.tolgaozgun.sprintplanning.databinding.FragmentJoinRoomBinding
import com.tolgaozgun.sprintplanning.viewmodels.lobby.join.JoinLobbyViewModel
import com.tolgaozgun.sprintplanning.viewmodels.lobby.join.JoinLobbyViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JoinLobbyFragment : Fragment() {

    private lateinit var binding: FragmentJoinRoomBinding
    private lateinit var viewModel: JoinLobbyViewModel
    private lateinit var viewModelFactory: JoinLobbyViewModelFactory



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
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

        binding.imgCamera.setOnClickListener {
            viewModel.replaceFragment(
                fragment = JoinLobbyCameraFragment(),
                shouldAddToBackStack = true,
            )
        }

        binding.btnJoinRoom.setOnClickListener {
            viewModel.joinRoom(binding.txtRoomIdInput.text.toString())
        }
    }

}