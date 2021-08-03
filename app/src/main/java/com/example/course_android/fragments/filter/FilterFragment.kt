package com.example.course_android.fragments.filter

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.course_android.Constants
import com.example.course_android.Constants.COUNTRY_AREA_END_KEY
import com.example.course_android.Constants.COUNTRY_AREA_START_KEY
import com.example.course_android.Constants.COUNTRY_NAME_KEY
import com.example.course_android.R
import com.example.course_android.databinding.FragmentCountryDetailsBinding
import com.example.course_android.databinding.FragmentFilterBinding
import com.example.course_android.dto.model.CountryDescriptionItemDto
import com.example.course_android.fragments.allCountries.AllCountriesViewModel
import com.example.course_android.fragments.allCountries.AllCountriesViewModelFactory
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider

class FilterFragment : Fragment() {

    private var binding: FragmentFilterBinding? = null
    private var slider: RangeSlider? = null
    private var valuesRangeSlider: MutableList<Float> = mutableListOf()
    private lateinit var viewModelFilter: FilterViewModel
    private var start = 0.0F
    private var end = 0.0F

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

        viewModelFilter =
            ViewModelProvider(this, FilterViewModelFactory())
                .get(FilterViewModel::class.java)
                .also {
                    it.countriesLiveData.observe(
                        viewLifecycleOwner,
                        Observer { data -> showResult(data) })
//                    it.countriesErrorLiveData.observe(
//                        viewLifecycleOwner,
//                        Observer { error -> showError(error) })
//
                }
//
//
        binding?.btnFilterGo?.setOnClickListener {

            viewModelFilter.getCountriesFromFilter(start, end)
//
        }

        slider?.addOnChangeListener { rangeSlider, value, fromUser ->
            start = rangeSlider.values[0]
            end = rangeSlider.values[1]
        }
    }

    private fun showResult(listCountriesFromApiDto: MutableList<CountryDescriptionItemDto>) {

        findNavController().navigate(R.id.action_filterFragment_to_secondFragment)

    }


}