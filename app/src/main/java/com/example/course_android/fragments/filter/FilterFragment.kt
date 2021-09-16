package com.example.course_android.fragments.filter

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.course_android.Constants.FILTER_VALUE_FROM_KEY_AREA
import com.example.course_android.Constants.FILTER_VALUE_FROM_KEY_POPULATION
import com.example.course_android.Constants.FILTER_VALUE_TO_KEY_AREA
import com.example.course_android.Constants.FILTER_VALUE_TO_KEY_POPULATION
import com.example.course_android.Constants.VALUE_OF_FILTER_KEY
import com.example.course_android.R
import com.example.course_android.base.mvvm.BaseMvvmView
import com.example.domain.outcome.Outcome
import com.example.course_android.databinding.FragmentFilterBinding
import com.example.course_android.ext.askLocationPermission
import com.example.course_android.ext.checkLocationPermission
import com.example.course_android.ext.isOnline
import com.example.course_android.ext.showAlertDialog
import com.example.course_android.utils.toast
import com.google.android.material.slider.RangeSlider
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import java.text.NumberFormat

private const val LOCATION_PERMISSION_CODE = 1000

class FilterFragment : ScopeFragment(R.layout.fragment_filter), BaseMvvmView {

    private var permissionGps = false
    private var binding: FragmentFilterBinding? = null
    private var sliderOfArea: RangeSlider? = null
    private var sliderOfPopulation: RangeSlider? = null
    private val viewModelFilter: FilterViewModel by stateViewModel()
    private var startArea = 0.0F
    private var endArea = 0.0F
    private var startPopulation = 0.0F
    private var endPopulation = 0.0F
    private var startDistance = 0
    private var endDistance = 0
    private lateinit var headerOfArea: TextView
    private lateinit var headerOfPopulation: TextView
    private lateinit var headerOfDistance: TextView
    private lateinit var viewDistanceFrom: EditText
    private lateinit var viewDistanceTo: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        headerOfArea = binding?.headerOfArea as TextView
        headerOfPopulation = binding?.headerOfPopulation as TextView
        headerOfDistance = binding?.headerOfArea as TextView
        viewDistanceFrom = binding?.distanceFrom as EditText
        viewDistanceTo = binding?.distanceTo as EditText

        sliderOfArea = binding?.sliderArea
        sliderOfArea?.setLabelFormatter { value: Float ->
            val format = NumberFormat.getIntegerInstance()
            format.maximumFractionDigits = 0
            format.format(value.toInt())
        }

        sliderOfPopulation = binding?.sliderPopulation
        sliderOfPopulation?.setLabelFormatter { value: Float ->
            val format = NumberFormat.getIntegerInstance()
            format.maximumFractionDigits = 0
            format.format(value.toInt())
        }
        viewModelFilter.makeConfigFilter()

        viewModelFilter.mutableFilterConfigLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Outcome.Progress -> {
                    binding?.progressFilter?.visibility = View.VISIBLE
                }
                is Outcome.Failure -> {
                    hideProgress()
                }
                is Outcome.Success -> {
                    buildFilterWithConfig(it.data)
                    hideProgress()
                }
                else -> {

                }
            }
        }
        return binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //проверяем и запрашиваем пермишен Gps
        if (context?.checkLocationPermission() == true) {
            permissionGps = true
        } else {
            activity?.askLocationPermission(LOCATION_PERMISSION_CODE)
        }

        //подписываеся на введенные юзером данные
        viewModelFilter.mutableFilterLiveData.observe(
            viewLifecycleOwner,
            Observer { data -> sendSettingsOfFilter(data) })

        //записываем введенные значения юзером в LiveData
        binding?.btnFilterGo?.setOnClickListener {
            startDistance = viewDistanceFrom.text.toString().toInt()
            endDistance = viewDistanceTo.text.toString().toInt()
            viewModelFilter.putValuesFromFilter(
                startArea,
                endArea,
                startDistance,
                endDistance,
                startPopulation,
                endPopulation
            )
        }

        //слушаем слайдер площади
        sliderOfArea?.addOnChangeListener { rangeSlider, value, fromUser ->
            startArea = rangeSlider.values[0]
            endArea = rangeSlider.values[1]
            headerOfArea.text = getString(R.string.area, startArea.toInt(), endArea.toInt())
        }

        //слушаем слайдер популяции
        sliderOfPopulation?.addOnChangeListener { rangeSlider, _, _ ->
            startPopulation = rangeSlider.values[0]
            endPopulation = rangeSlider.values[1]
            headerOfPopulation.text =
                getString(R.string.population, startPopulation.toInt(), endPopulation.toInt())
        }
    }

    //отправляем конфиг в предыдущий фрагмент
    private fun sendSettingsOfFilter(map: HashMap<String, Int>) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(VALUE_OF_FILTER_KEY, map)
        findNavController().popBackStack()
    }

    //формируем начальные и конечные данные фильтра
    private fun buildFilterWithConfig(mapOfConfig: HashMap<String, Float>) {
        sliderOfArea?.valueFrom = mapOfConfig[FILTER_VALUE_FROM_KEY_AREA] ?: 0.0F
        sliderOfArea?.valueTo = mapOfConfig[FILTER_VALUE_TO_KEY_AREA] ?: 0.0F
        sliderOfArea?.values =
            listOf(mapOfConfig[FILTER_VALUE_FROM_KEY_AREA], mapOfConfig[FILTER_VALUE_TO_KEY_AREA])
        headerOfArea.text = getString(
            R.string.area,
            mapOfConfig[FILTER_VALUE_FROM_KEY_AREA]?.toInt(),
            mapOfConfig[FILTER_VALUE_TO_KEY_AREA]?.toInt()
        )
        sliderOfPopulation?.valueFrom = mapOfConfig[FILTER_VALUE_FROM_KEY_POPULATION] ?: 0.0F
        sliderOfPopulation?.valueTo = mapOfConfig[FILTER_VALUE_TO_KEY_POPULATION] ?: 0.0F
        sliderOfPopulation?.values =
            listOf(
                mapOfConfig[FILTER_VALUE_FROM_KEY_POPULATION],
                mapOfConfig[FILTER_VALUE_TO_KEY_POPULATION]
            )
        headerOfPopulation.text = getString(
            R.string.population,
            mapOfConfig[FILTER_VALUE_FROM_KEY_POPULATION]?.toInt(),
            mapOfConfig[FILTER_VALUE_TO_KEY_POPULATION]?.toInt()
        )
    }

    override fun showError() {
        hideProgress()
        if (context?.isOnline() == false) {
            context?.toast(getString(R.string.chek_inet))
        } else {
            activity?.showAlertDialog()
        }
    }

    override fun showProgress() {
        binding?.progressFilter?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding?.progressFilter?.visibility = View.GONE
    }
}