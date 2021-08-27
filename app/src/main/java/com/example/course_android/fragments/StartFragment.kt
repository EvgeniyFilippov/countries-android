package com.example.course_android.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.course_android.R
import com.example.course_android.databinding.FragmentStartBinding
import com.example.course_android.ext.askLocationPermission
import com.example.course_android.ext.checkLocationPermission



class StartFragment : Fragment(R.layout.fragment_start) {

    private var binding: FragmentStartBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStartBinding.bind(view)
        binding?.btnMain?.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_secondFragment)
        }
        binding?.btnMap?.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_mapOfAllCountriesFragment2)
        }
        binding?.btnCapitals?.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_allCapitalsFragment)
        }
        binding?.btnNews?.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_newsFragment)
        }
        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item1) {
            findNavController().navigate(R.id.action_startFragment_to_secondFragment)
        } else if (item.itemId == R.id.item2) {
            findNavController().navigate(R.id.action_startFragment_to_mapOfAllCountriesFragment2)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}