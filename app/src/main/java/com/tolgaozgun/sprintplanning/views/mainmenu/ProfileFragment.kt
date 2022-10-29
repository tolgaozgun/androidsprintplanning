package com.tolgaozgun.sprintplanning.views.mainmenu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.tolgaozgun.sprintplanning.databinding.FragmentProfileBinding
import com.tolgaozgun.sprintplanning.viewmodels.mainmenu.ProfileViewModel
import com.tolgaozgun.sprintplanning.viewmodels.mainmenu.ProfileViewModelFactory

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var viewModelFactory: ProfileViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        viewModelFactory = ProfileViewModelFactory(fragmentManager = fragmentManager,
            context = requireContext())
        viewModel = viewModelFactory.create(ProfileViewModel::class.java)

        binding.edtName.setText(viewModel.getUsername(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSave.setOnClickListener {
            val result: Boolean = viewModel.saveChanges(binding.edtName.text.toString(), null)
            if(result){
                Toast.makeText(requireContext(), "Successfully saved changes!", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(requireContext(), "Error while saving changes!", Toast.LENGTH_LONG).show()
            }
        }
    }

}