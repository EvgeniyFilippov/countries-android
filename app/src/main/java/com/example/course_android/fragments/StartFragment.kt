package com.example.course_android.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.course_android.R
import com.example.course_android.databinding.FragmentStartBinding
import com.example.course_android.ext.askLocationPermission
import com.example.course_android.ext.checkLocationPermission
import com.example.course_android.services.LocationTrackingService
import com.example.course_android.utils.animateButton
import com.example.course_android.utils.animateMainPicture
import com.example.course_android.utils.makeVisibility

private const val LOCATION_PERMISSION_CODE = 1000

class StartFragment : Fragment(R.layout.fragment_start) {

    private val mLocationBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null && intent.action != null) {
                when (intent.action) {
                    LocationTrackingService.NEW_LOCATION_ACTION -> {
                        Log.e(
                            "YF service GPS: ",
                            intent.getParcelableExtra<Location>("location").toString()
                        )
                    }
                }
            }
        }
    }

    private var binding: FragmentStartBinding? = null
    private var alreadyExecuted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intentFilter = IntentFilter()
        intentFilter.addAction(LocationTrackingService.NEW_LOCATION_ACTION)
        context?.registerReceiver(mLocationBroadcastReceiver, intentFilter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentStartBinding.bind(view)

        val listOfButton = listOf(
            binding?.btnMain,
            binding?.btnMap,
            binding?.btnCapitals,
            binding?.btnNews,
            binding?.btnNewsLocal
        )

        val listOfPicture = listOf(
            binding?.earth01,
            binding?.earth02,
            binding?.earth03,
            binding?.earth04
        )

        if (!alreadyExecuted) {

            makeVisibility(listOfButton, false)
            animateMainPicture(listOfPicture, listOfButton)
            alreadyExecuted = true

        }

        if (context?.checkLocationPermission() == false) {
            activity?.askLocationPermission(LOCATION_PERMISSION_CODE)
        }

        binding?.btnMain?.setOnClickListener {
            animateButton(it)
            { findNavController().navigate(R.id.action_startFragment_to_secondFragment) }
        }

        binding?.btnMap?.setOnClickListener {
            animateButton(it)
            { findNavController().navigate(R.id.action_startFragment_to_mapOfAllCountriesFragment2) }
        }
        binding?.btnCapitals?.setOnClickListener {
            animateButton(it)
            { findNavController().navigate(R.id.action_startFragment_to_allCapitalsFragment) }
        }
        binding?.btnNews?.setOnClickListener {
            animateButton(it)
            { findNavController().navigate(R.id.action_startFragment_to_newsFragment) }
        }

        binding?.btnNewsLocal?.setOnClickListener {
            animateButton(it)
            { findNavController().navigate(R.id.action_startFragment_to_newsByLocationFragment) }
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(mLocationBroadcastReceiver)
    }
}