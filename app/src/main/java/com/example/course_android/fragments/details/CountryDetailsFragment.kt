package com.example.course_android.fragments.details

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.course_android.Constants.COUNTRY_NAME_KEY
import com.example.course_android.Constants.ERROR
import com.example.course_android.R
import com.example.course_android.adapters.AdapterLanguages
import com.example.course_android.base.googlemap.getDistance
import com.example.course_android.base.googlemap.initMap2
import com.example.course_android.base.mvp.BaseMvpFragment
import com.example.course_android.databinding.FragmentCountryDetailsBinding
import com.example.course_android.dto.model.CountryDescriptionItemDto
import com.example.course_android.ext.askLocationPermission
import com.example.course_android.ext.showDialogWithOneButton
import com.example.course_android.utils.loadSvg
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.model.LatLng
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_country_details.*
import com.example.course_android.ext.checkLocationPermission

private const val LOCATION_PERMISSION_CODE = 1000

class CountryDetailsFragment : BaseMvpFragment<CountryDetailsView, CountryDetailsPresenter>(), CountryDetailsView {

    private lateinit var mCountryName: String
    private var binding: FragmentCountryDetailsBinding? = null
    private var mSrCountryDetails: SwipeRefreshLayout? = null
    private var progressBar: FrameLayout? = null
    private val mCompositeDisposable = CompositeDisposable()
    var mapFragment: SupportMapFragment? = null
    private var adapterLanguages = AdapterLanguages()
    private var permissionGps = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mCountryName = arguments?.getString(COUNTRY_NAME_KEY) ?: ERROR

        binding = FragmentCountryDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPresenter().attachView(this)
        setHasOptionsMenu(true)
        binding?.mTvCountryName?.text = mCountryName
        mSrCountryDetails = binding?.srCountryDetails
        progressBar = binding?.progress
        recycler_languages.layoutManager = LinearLayoutManager(context)
        recycler_languages.adapter = adapterLanguages

        mSrCountryDetails?.setOnRefreshListener {
            getPresenter().getMyData(mCountryName, true)
        }
        getPresenter().getMyData(mCountryName, false)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.country_description_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.gps_distance) {
            activity?.showDialogWithOneButton(
                null,
                getString(R.string.distanceToYou, mCountryName, getDistance()),
                R.string.dialog_ok,
                null
            )
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        mCompositeDisposable.clear()
    }

    override fun createPresenter() {
        mPresenter = CountryDetailsPresenter()
    }

    override fun getPresenter(): CountryDetailsPresenter = mPresenter

    override fun showCountryInfo(country: CountryDescriptionItemDto) {
        //языки ресайкл
        adapterLanguages.repopulate(country.languages)

        binding?.srCountryDetails?.isRefreshing = false

        //флаг
        binding?.itemFlag?.loadSvg(country.flag)

        //проверяем и запрашиваем пермишен Gps
        if (context?.checkLocationPermission() == true) {
    permissionGps = true
                getDistance()
            } else {
                activity?.askLocationPermission(LOCATION_PERMISSION_CODE)
            }

        //карта гугл
        mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragmentContainer) as? SupportMapFragment?
        mapFragment?.run {
            getMapAsync { map -> activity?.let { initMap2(map, country, it.applicationContext, permissionGps) } }
        }
    }

    override fun showError(error: String, throwable: Throwable) {
//        showAlertDialog()
    }

    override fun showProgress() {
        binding?.progress?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding?.progress?.visibility = View.GONE
    }

}