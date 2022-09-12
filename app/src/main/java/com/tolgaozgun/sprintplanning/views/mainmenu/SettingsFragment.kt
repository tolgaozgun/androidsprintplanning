package com.tolgaozgun.sprintplanning.views.mainmenu

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.tolgaozgun.sprintplanning.R
import com.tolgaozgun.sprintplanning.databinding.FragmentSettingsBinding
import com.tolgaozgun.sprintplanning.viewmodels.mainmenu.SettingsViewModel
import com.tolgaozgun.sprintplanning.viewmodels.mainmenu.SettingsViewModelFactory
import org.w3c.dom.Text

class SettingsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsViewModel
    private lateinit var viewModelFactory: SettingsViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        viewModelFactory = SettingsViewModelFactory(fragmentManager = fragmentManager)
        viewModel = viewModelFactory.create(SettingsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtContactUs.setOnClickListener {
            composeEmail()
        }

        binding.txtContactUsDesc.setOnClickListener {
            composeEmail()
        }

//        val spinner: Spinner = binding.spinnerThemeSelector
//        ArrayAdapter.createFromResource(
//            requireContext(),
//            R.array.themes,
//            android.R.layout.simple_spinner_item
//        ).also{ adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            spinner.adapter = adapter
//            spinner.onItemSelectedListener = this
//        }
    }

    fun composeEmail(){
        var intent: Intent = Intent(Intent.ACTION_SENDTO)
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, "tolgaozgunn@gmail.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Sprint Planning Feedback");
        startActivity(intent)
    }

    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        var inflater = super.onGetLayoutInflater(savedInstanceState)
        val contextThemeWrapper: Context = ContextThemeWrapper(requireContext(), R.style.Theme_SprintPlanning_Night)
        return inflater.cloneInContext(contextThemeWrapper)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        val theme: Int = when(pos) {
            1 -> {
                R.style.Theme_SprintPlanning
            }
            2 -> {
                R.style.Theme_SprintPlanning_Night
            }
            else -> {
                R.style.Theme_SprintPlanning
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

}