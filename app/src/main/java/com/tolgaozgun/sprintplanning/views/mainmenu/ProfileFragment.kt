package com.tolgaozgun.sprintplanning.views.mainmenu

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.set
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.tolgaozgun.sprintplanning.data.model.User
import com.tolgaozgun.sprintplanning.databinding.FragmentProfileBinding
import com.tolgaozgun.sprintplanning.viewmodels.mainmenu.ProfileViewModel
import com.tolgaozgun.sprintplanning.viewmodels.mainmenu.ProfileViewModelFactory
import kotlinx.coroutines.selects.select

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

        setupObserver()
        viewModel.loadUser()
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
                Toast.makeText(requireContext(), "Successfully saved changes!", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), "Error while saving changes!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imgAvatar.setOnClickListener{
            selectImage()
        }


    }

    private fun setupObserver(){
        viewModel.user.observe(viewLifecycleOwner, Observer<User> { user ->
            binding.edtName.setText(user.name)
            binding.imgAvatar.setImageBitmap(user.avatar)
        })
    }

    private fun selectImage(){
        val intent: Intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == Activity.RESULT_OK){
            var imageUri: Uri = data?.data!!
            Log.d("URL", "1: ${data.dataString} 2: ${data.action} 3:${data.type} " +
                    "4:${data.extras} 5:${data.clipData} 6:${data.flags} 7:${data.data!!.encodedPath}" +
                    "8:${data.data!!.path} 9:${data.data!!.query}")
            viewModel.uploadImage(imageUri)
        }
    }


}