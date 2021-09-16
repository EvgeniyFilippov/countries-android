package com.example.course_android.fragments.map

import android.os.Bundle
import com.example.course_android.ext.showAlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.course_android.R
import com.example.course_android.base.mvp.BaseMvpFragment
import com.example.course_android.databinding.FragmentMapAllCountriesBinding
import com.example.domain.dto.model.CountryDescriptionItemDto
import com.example.course_android.ext.isOnline
import com.example.course_android.utils.toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject

class MapAllCountriesFragment : BaseMvpFragment<MapAllCountriesView, MapAllCountriesPresenter>(),
    MapAllCountriesView, OnMapReadyCallback {

    private var binding: FragmentMapAllCountriesBinding? = null
    private var mapFragment: SupportMapFragment? = null
    private val mCompositeDisposable = CompositeDisposable()
    private val mModulePresenter: MapAllCountriesPresenter by inject()
    private lateinit var listCountries: List<CountryDescriptionItemDto>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapAllCountriesBinding.inflate(inflater, container, false)
        mapFragment =
            childFragmentManager.findFragmentById(R.id.allMapFragmentContainer) as? SupportMapFragment?

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPresenter().attachView(this)
        setHasOptionsMenu(true)
        getPresenter().getAllCountries()
    }

    override fun createPresenter() {
        mPresenter = mModulePresenter
    }

    override fun getPresenter(): MapAllCountriesPresenter = mPresenter

    override fun showAllCountriesOnMap(list: List<CountryDescriptionItemDto>) {
        listCountries = list
        mapFragment?.getMapAsync(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        mCompositeDisposable.clear()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        listCountries.forEach { country ->
            googleMap?.addMarker(
                MarkerOptions()
                    .position(LatLng(country.latlng[0], country.latlng[1]))
                    .title(country.name)
            )
        }
        hideProgress()
    }

    override fun showError(error: String, throwable: Throwable) {
        if (context?.isOnline() == false) {
            context?.toast(getString(R.string.chek_inet))
        } else {
            activity?.showAlertDialog()
        }
    }

    override fun showProgress() {
        binding?.progressMap?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding?.progressMap?.visibility = View.GONE
    }
}