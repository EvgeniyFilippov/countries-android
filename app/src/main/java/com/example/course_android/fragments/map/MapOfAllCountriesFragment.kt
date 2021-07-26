package com.example.course_android.fragments.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.course_android.R
import com.example.course_android.base.googlemap.initMap2
import com.example.course_android.base.googlemap.initMap3
import com.example.course_android.base.mvp.BaseMvpFragment
import com.example.course_android.databinding.FragmentCountryDetailsBinding

import com.example.course_android.databinding.FragmentMapOfAllCountriesBinding
import com.example.course_android.fragments.details.CountryDetailsPresenter
import com.example.course_android.model.allCountries.CountriesDataItem
import com.google.android.libraries.maps.SupportMapFragment
import kotlinx.android.synthetic.main.fragment_country_details.*

class MapOfAllCountriesFragment : BaseMvpFragment<MapAllCountriesView, MapAllCountriesPresenter>(), MapAllCountriesView {

    private var binding: FragmentMapOfAllCountriesBinding? = null
    var mapFragment: SupportMapFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapOfAllCountriesBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_map_of_all_countries, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPresenter().attachView(this)
        setHasOptionsMenu(true)
        binding?.srCountryMap?.setOnRefreshListener {
            getPresenter().getAllCountries(true)
        }
        getPresenter().getAllCountries(false)

    }

    override fun createPresenter() {
        mPresenter = MapAllCountriesPresenter()
    }

    override fun getPresenter(): MapAllCountriesPresenter = mPresenter

    override fun showAllCountriesOnMap(listOfCountries: List<CountriesDataItem>) {
        //карта гугл
        mapFragment =
            childFragmentManager.findFragmentById(R.id.allMapFragmentContainer) as? SupportMapFragment?
        mapFragment?.run {
            getMapAsync { map -> activity?.let { initMap3(map, listOfCountries, it.applicationContext) } }
        }
    }

    override fun showError(error: String, throwable: Throwable) {
        TODO("Not yet implemented")
    }

    override fun showProgress() {
        binding?.progressMap?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding?.progressMap?.visibility = View.GONE
    }


}