package com.example.course_android.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.course_android.Constants.DEFAULT_DOUBLE
import com.example.course_android.R
import com.example.course_android.databinding.FragmentStartBinding
import com.example.course_android.ext.askLocationPermission
import com.example.course_android.ext.checkLocationPermission
import com.example.course_android.services.LocationTrackingService

private const val LOCATION_PERMISSION_CODE = 1000

class StartFragment : Fragment(R.layout.fragment_start) {

    private val mLocationBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null && intent.action != null) {
                when (intent.action) {
                    LocationTrackingService.NEW_LOCATION_ACTION -> {
                        Log.e("YF service GPS: ", intent.getParcelableExtra<Location>("location").toString())
                    }
                }
            }
        }
    }

    private var binding: FragmentStartBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intentFilter = IntentFilter()
        intentFilter.addAction(LocationTrackingService.NEW_LOCATION_ACTION)
        context?.registerReceiver(mLocationBroadcastReceiver, intentFilter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStartBinding.bind(view)

        if (context?.checkLocationPermission() == false) {
            activity?.askLocationPermission(LOCATION_PERMISSION_CODE)
        }

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.context?.startForegroundService(
                Intent(
                    this.context,
                    LocationTrackingService::class.java
                )
            )
        } else {
            this.context?.startService(
                Intent(
                    this.context,
                    LocationTrackingService::class.java
                )
            )
        }

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

    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(mLocationBroadcastReceiver)
    }
}