package com.tolgaozgun.sprintplanning.views.mainmenu

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tolgaozgun.sprintplanning.R
import com.tolgaozgun.sprintplanning.databinding.ActivityMainBinding
import com.tolgaozgun.sprintplanning.databinding.FragmentCreateRoomBinding
import com.tolgaozgun.sprintplanning.viewmodels.lobby.create.CreateLobbyViewModel
import com.tolgaozgun.sprintplanning.viewmodels.lobby.create.CreateLobbyViewModelFactory
import com.tolgaozgun.sprintplanning.viewmodels.mainmenu.MainViewModel
import com.tolgaozgun.sprintplanning.viewmodels.mainmenu.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager: FragmentManager = supportFragmentManager
        viewModelFactory = MainViewModelFactory(fragmentManager = fragmentManager, context = this)
        viewModel = viewModelFactory.create(MainViewModel::class.java)

        viewModel.checkFirstTime(this)
        viewModel.replaceFragment(HomeFragment())


        // TODO: If the user is currently in a room, do not load Home Fragment, but load
        // the Room Fragment with room information instead.
        binding.navigationBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> viewModel.replaceFragment(HomeFragment())
                R.id.profile -> viewModel.replaceFragment(ProfileFragment())
                R.id.settings -> viewModel.replaceFragment(SettingsFragment())
                else -> {}
            }
            true
        }


    }

}