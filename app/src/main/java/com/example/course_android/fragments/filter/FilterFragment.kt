package com.example.course_android.fragments.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.course_android.Constants.FILTER_VALUE_FROM_KEY
import com.example.course_android.Constants.FILTER_VALUE_TO_KEY
import com.example.course_android.R
import com.example.course_android.databinding.FragmentFilterBinding
import com.google.android.material.slider.RangeSlider
import java.text.NumberFormat
import kotlin.collections.HashMap

class FilterFragment : Fragment() {

    private var binding: FragmentFilterBinding? = null
    private var slider: RangeSlider? = null
    private var valuesRangeSlider: MutableList<Float> = mutableListOf()
    private lateinit var viewModelFilter: FilterViewModel
    private var start = 0.0F
    private var end = 0.0F
    private lateinit var headerOfArea: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFilter =
            ViewModelProvider(this, FilterViewModelFactory())
                .get(FilterViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        headerOfArea = binding?.header1 as TextView
        slider = binding?.slider
        slider?.setLabelFormatter { value: Float ->
            val format = NumberFormat.getIntegerInstance()
            format.maximumFractionDigits = 0
            format.format(value.toInt())
        }
        viewModelFilter.mutableFilterConfigLiveData.observe(
            viewLifecycleOwner,
            Observer { data -> buildFilterWithConfig(data) })
        viewModelFilter.makeConfigFilter()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModelFilter.mutableFilterLiveData.observe(
            viewLifecycleOwner,
            Observer { data -> sendSettingsOfFilter(data) })





        binding?.btnFilterGo?.setOnClickListener {

            viewModelFilter.getCountriesFromFilter(start, end)

//
        }

        slider?.addOnChangeListener { rangeSlider, value, fromUser ->
            start = rangeSlider.values[0]
            end = rangeSlider.values[1]
            headerOfArea.text = getString(R.string.area, start.toInt(), end.toInt())
        }
    }

    private fun sendSettingsOfFilter(map: HashMap<String, Int>) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set("map", map)
        findNavController().popBackStack()
    }

    private fun buildFilterWithConfig(mapOfConfig: HashMap<String, Float>) {

        slider?.valueFrom = mapOfConfig[FILTER_VALUE_FROM_KEY]!!
        slider?.valueTo = mapOfConfig[FILTER_VALUE_TO_KEY]!!
        slider?.values =
            listOf(mapOfConfig[FILTER_VALUE_FROM_KEY], mapOfConfig[FILTER_VALUE_TO_KEY])
        headerOfArea.text = getString(
            R.string.area,
            mapOfConfig[FILTER_VALUE_FROM_KEY]?.toInt(),
            mapOfConfig[FILTER_VALUE_TO_KEY]?.toInt()
        )

    }
}