package com.example.course_android.fragments.filter

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.course_android.Constants
import com.example.course_android.Constants.COUNTRY_AREA_END_KEY
import com.example.course_android.Constants.COUNTRY_AREA_START_KEY
import com.example.course_android.Constants.COUNTRY_NAME_KEY
import com.example.course_android.R
import com.example.course_android.databinding.FragmentCountryDetailsBinding
import com.example.course_android.databinding.FragmentFilterBinding
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider

class FilterFragment : Fragment() {

    private var binding: FragmentFilterBinding? = null
    private var slider: RangeSlider? = null
    private var valuesRangeSlider: MutableList<Float> = mutableListOf()

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
            val bundle = Bundle()
            val t1 = valuesRangeSlider[0]
            val t2 = valuesRangeSlider[1]
                bundle.putString(COUNTRY_AREA_START_KEY, t1.toString())
//            bundle.putFloat(COUNTRY_AREA_END_KEY,t2)
            findNavController().navigate(R.id.action_filterFragment_to_secondFragment)
        }

//
//        slider?.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
//            override fun onStartTrackingTouch(slider: RangeSlider) {
//                // Responds to when slider's touch event is being started
//            }
//
//            override fun onStopTrackingTouch(slider: RangeSlider) {
//                // Responds to when slider's touch event is being stopped
//
//            }
//        })

        slider?.addOnChangeListener { rangeSlider, value, fromUser ->
            valuesRangeSlider = rangeSlider.values
            // Responds to when slider's value is changed
        }
    }
}