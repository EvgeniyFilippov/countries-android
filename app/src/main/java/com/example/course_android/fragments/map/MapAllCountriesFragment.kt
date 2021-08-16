package com.example.course_android.fragments.map

import android.os.Bundle
import com.example.course_android.ext.showAlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.course_android.R
import com.example.course_android.utils.initMapOfAllCountries
import com.example.course_android.base.mvp.BaseMvpFragment
import com.example.course_android.databinding.FragmentMapAllCountriesBinding
import com.example.domain.dto.model.CountryDescriptionItemDto
import com.example.course_android.ext.isOnline
import com.example.course_android.utils.toast
import com.google.android.libraries.maps.SupportMapFragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject

class MapAllCountriesFragment : BaseMvpFragment<MapAllCountriesView, MapAllCountriesPresenter>(),
    MapAllCountriesView {

    private var binding: FragmentMapAllCountriesBinding? = null
    private var mapFragment2: SupportMapFragment? = null
    private val mCompositeDisposable = CompositeDisposable()
    private val mModulePresenter : MapAllCountriesPresenter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapAllCountriesBinding.inflate(inflater, container, false)
        mapFragment2 =
            childFragmentManager.findFragmentById(R.id.allMapFragmentContainer) as? SupportMapFragment?
        return binding?.root
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
        mPresenter = mModulePresenter
    }

    override fun getPresenter(): MapAllCountriesPresenter = mPresenter

    override fun showAllCountriesOnMap(listOfCountries: List<CountryDescriptionItemDto>) {
        binding?.srCountryMap?.isRefreshing = false

        //карта гугл
        mapFragment2?.run {
            getMapAsync { map -> activity?.let { initMapOfAllCountries(map, listOfCountries) } }
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        mCompositeDisposable.clear()
    }
}