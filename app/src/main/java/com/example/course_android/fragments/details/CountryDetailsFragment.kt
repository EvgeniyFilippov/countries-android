package com.example.course_android.fragments.details

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.course_android.Constants.COUNTRY_NAME_KEY
import com.example.course_android.Constants.ERROR
import com.example.course_android.R
import com.example.course_android.adapters.AdapterLanguages
import com.example.course_android.utils.getDistance
import com.example.course_android.utils.initMap2
//import com.example.course_android.utils.initMap2
import com.example.course_android.base.mvp.BaseMvpFragment
import com.example.course_android.databinding.FragmentCountryDetailsBinding
import com.example.course_android.dto.model.CountryDescriptionItemDto
import com.example.course_android.ext.askLocationPermission
import com.example.course_android.ext.checkLocationPermission
import com.example.course_android.ext.isOnline
import com.example.course_android.ext.showDialogWithOneButton
import com.example.course_android.utils.loadSvg
import com.example.course_android.utils.toast
import com.google.android.libraries.maps.SupportMapFragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_country_details.*

private const val LOCATION_PERMISSION_CODE = 1000

class CountryDetailsFragment : BaseMvpFragment<CountryDetailsView, CountryDetailsPresenter>(),
    CountryDetailsView {

    private lateinit var mCountryName: String
    private var binding: FragmentCountryDetailsBinding? = null

    //    private var progressBar: FrameLayout? = null
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
        mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragmentContainer) as? SupportMapFragment?
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPresenter().attachView(this)
        setHasOptionsMenu(true)
        binding?.mTvCountryName?.text = mCountryName
        recycler_languages.layoutManager = LinearLayoutManager(context)
        recycler_languages.adapter = adapterLanguages

        binding?.srCountryDetails?.setOnRefreshListener {
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

    override fun showCountryInfo(country: List<CountryDescriptionItemDto>) {
        //языки ресайкл
        adapterLanguages.repopulate(country[0].languages)

        binding?.srCountryDetails?.isRefreshing = false

        //флаг
        binding?.itemFlag?.loadSvg(country[0].flag)

        //проверяем и запрашиваем пермишен Gps
        if (context?.checkLocationPermission() == true) {
            permissionGps = true
            getDistance()
        } else {
            activity?.askLocationPermission(LOCATION_PERMISSION_CODE)
        }

        //карта гугл
        mapFragment?.run {
            getMapAsync { map ->
                activity?.let {
                    initMap2(
                        map,
                        country[0],
                        it.applicationContext,
                        permissionGps
                    )
                }
            }
        }
    }

    override fun showError(error: String, throwable: Throwable) {
        if (context?.isOnline() == false) {
            context?.toast(getString(R.string.chek_inet))
        }
    }

    override fun showProgress() {
        binding?.progress?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding?.progress?.visibility = View.GONE
    }

}