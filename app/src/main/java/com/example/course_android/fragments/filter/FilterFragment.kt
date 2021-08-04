package com.example.course_android.fragments.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.course_android.R
import com.example.course_android.databinding.FragmentFilterBinding
import com.google.android.material.slider.RangeSlider

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
                    it.mutableCountriesLiveData.observe(
                        viewLifecycleOwner,
                        Observer { data -> sendSettingsOfFilter(data) })
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

    private fun sendSettingsOfFilter(map: HashMap<String, Float>) {

//        var gson = Gson()
//        var jsonString = gson.toJson(listCountriesFromApiDto)
//        val bundle = Bundle()
//
//        val extras = Bundle()
//        extras.putSerializable("HashMap", map)
//        intent.putExtras(extras)
//
//        bundle.put("tutu", jsonString)

        findNavController().previousBackStackEntry?.savedStateHandle?.set("map", map)


        findNavController().popBackStack()

    }


}