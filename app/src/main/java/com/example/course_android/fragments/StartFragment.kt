package com.example.course_android.fragments

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.course_android.R
import com.example.course_android.databinding.FragmentStartBinding
import com.example.course_android.ext.showAlertDialogWithMessage
import com.example.course_android.services.LocationTrackingService
import com.example.course_android.utils.animateButton
import com.example.course_android.utils.animateMainPicture
import com.example.course_android.utils.makeVisibilityButtons
import com.example.course_android.utils.makeVisibilityPictures

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

    val singlePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    if (!alreadyExecuted) {
                        animateMainPicture(listOfPicture, listOfButton)
                    }
                    alreadyExecuted = true
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
                !shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) -> {

                }
                else -> {

                }
            }
        }

    private var binding: FragmentStartBinding? = null
    private var alreadyExecuted = false
    private lateinit var listOfPicture: List<AppCompatImageView?>
    private lateinit var listOfButton: List<AppCompatButton?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intentFilter = IntentFilter()
        intentFilter.addAction(LocationTrackingService.NEW_LOCATION_ACTION)
        context?.registerReceiver(mLocationBroadcastReceiver, intentFilter)



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentStartBinding.bind(view)

        listOfButton = listOf(
            binding?.btnMain,
            binding?.btnMap,
            binding?.btnCapitals,
            binding?.btnNews,
            binding?.btnNewsLocal
        )

        listOfPicture = listOf(
            binding?.earth01,
            binding?.earth02,
            binding?.earth03,
            binding?.earth04
        )

        if (!alreadyExecuted) {
            makeVisibilityButtons(listOfButton, false)
            makeVisibilityPictures(listOfPicture, false)
        }

        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
            activity?.showAlertDialogWithMessage(getString(R.string.explain_why_access_is_needed))
        } else {
            singlePermission.launch(ACCESS_FINE_LOCATION)
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