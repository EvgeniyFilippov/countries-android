package com.example.course_android.fragments.filter

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.course_android.R
import com.example.course_android.databinding.FragmentCountryDetailsBinding
import com.example.course_android.databinding.FragmentFilterBinding
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider

class FilterFragment : Fragment() {

    private var binding: FragmentFilterBinding? = null
    private var slider: RangeSlider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        slider = binding?.slider
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnFilterGo?.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_secondFragment)
        }

        slider?.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being stopped

            }
        })

        slider?.addOnChangeListener { rangeSlider, value, fromUser ->
            // Responds to when slider's value is changed
        }
    }
}