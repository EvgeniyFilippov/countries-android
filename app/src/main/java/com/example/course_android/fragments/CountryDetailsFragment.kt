package com.example.course_android.fragments

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.course_android.Constants.COUNTRY_NAME_KEY
import com.example.course_android.Constants.DEFAULT_SLEEP
import com.example.course_android.Constants.ERROR
import com.example.course_android.CountriesApp
import com.example.course_android.CountriesApp.Companion.adapterLanguages
import com.example.course_android.R
import com.example.course_android.api.CountryDescriptionApi
import com.example.course_android.api.RetrofitObj
import com.example.course_android.databinding.FragmentCountryDetailsBinding
import com.example.course_android.dto.CountryDetailsDtoTransformer
import com.example.course_android.dto.model.CountryDescriptionItemDto
import com.example.course_android.dto.model.LanguageOfOneCountryDto
import com.example.course_android.ext.showDialogWithOneButton
import com.example.course_android.model.oneCountry.CountryDescriptionItem
import com.example.course_android.utils.loadSvg
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_country_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val LOCATION_PERMISSION_CODE = 1000
private const val LOG_TAG = "CountryDetailsFragment"

class CountryDetailsFragment : Fragment(R.layout.fragment_country_details) {

    private lateinit var mCountryName: String
    private var binding: FragmentCountryDetailsBinding? = null
    private var mLanguageList: List<LanguageOfOneCountryDto>? = null
    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var countryDescriptionFromApi: MutableList<CountryDescriptionItem>
    private lateinit var countryDetailsDto: CountryDescriptionItemDto
    private var mSrCountryDetails: SwipeRefreshLayout? = null
    private var progressBar: FrameLayout? = null

    private var googleMap: GoogleMap? = null
    var mapFragment: SupportMapFragment? = null
    private var distance: Int = 0

    private lateinit var currentCountryLatLng: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCountryName = arguments?.getString(COUNTRY_NAME_KEY) ?: ERROR
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentCountryDetailsBinding.bind(view)
        binding?.mTvCountryName?.text = mCountryName
        mSrCountryDetails = binding?.srCountryDetails
        progressBar = binding?.progress
        mSrCountryDetails?.setOnRefreshListener {
            getMyData(true)
        }
        getMyData(false)
    }

    private fun initMap(map: GoogleMap) {
        currentCountryLatLng = LatLng(
            countryDetailsDto.latlng[0],
            countryDetailsDto.latlng[1]
        )
        googleMap = map.apply {

            val cameraLocation = CameraUpdateFactory.newLatLngZoom(currentCountryLatLng, 7.0f)
            moveCamera(cameraLocation)
            if (checkLocationPermission()) {
                isMyLocationEnabled = true
                getDistance()
            } else {
                askLocationPermission()
            }
        }
        addMarkerOnMap(currentCountryLatLng)
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_CODE && grantResults[0] == PERMISSION_GRANTED) {
            googleMap?.isMyLocationEnabled = true
            getDistance()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    //проверяем permission
    private fun checkLocationPermission() =
        context?.let {
            ContextCompat.checkSelfPermission(
                it,
                ACCESS_FINE_LOCATION
            )
        } == PERMISSION_GRANTED

    //запрос permission
    private fun askLocationPermission() {
        requestPermissions(arrayOf(ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE)
    }

    //добавляем маркер
    private fun addMarkerOnMap(markerPosition: LatLng) {
        val markerOptions = MarkerOptions()
            .position(markerPosition)
            .title("$mCountryName")
        googleMap?.addMarker(markerOptions)
    }

    private fun getMyData(isRefresh: Boolean) {
        progressBar?.visibility = if (isRefresh) View.GONE else View.VISIBLE
        RetrofitObj.getOkHttp()
        val countryDescrApi = CountriesApp.retrofit.create(CountryDescriptionApi::class.java)
        val countryDescrApiCall = countryDescrApi.getTopHeadlines(mCountryName)
        countryDescrApiCall.enqueue(object : Callback<List<CountryDescriptionItem>?> {
            override fun onResponse(
                call: Call<List<CountryDescriptionItem>?>,
                response: Response<List<CountryDescriptionItem>?>
            ) {
                if (response.body() != null) {
                    countryDescriptionFromApi =
                        (response.body() as MutableList<CountryDescriptionItem>)
                    mSrCountryDetails?.isRefreshing = false
                    //трансформируем в DTO
                    val countryDetailsDtoTransformer = CountryDetailsDtoTransformer()
                    countryDetailsDto =
                        countryDetailsDtoTransformer.transform(countryDescriptionFromApi)

                    //языки ресайкл
                    linearLayoutManager = LinearLayoutManager(context)
                    recycler_languages.layoutManager = linearLayoutManager

                    mLanguageList = countryDetailsDto.languages
                    recycler_languages.adapter = adapterLanguages
                    adapterLanguages.repopulate(mLanguageList as MutableList<LanguageOfOneCountryDto>)

                    //флаг
                    binding?.itemFlag?.loadSvg(countryDetailsDto.flag)

                    //карта гугл
                    mapFragment =
                        childFragmentManager.findFragmentById(R.id.mapFragmentContainer) as? SupportMapFragment?
                    mapFragment?.run {
                        getMapAsync { map -> initMap(map) }
                    }

                } else {
                    Log.d("RETROFIT_COUNTRIES", response.body().toString())
                }
                Thread.sleep(DEFAULT_SLEEP)
                progressBar?.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<CountryDescriptionItem>?>, t: Throwable) {
                Log.d("RETROFIT_COUNTRIES", t.toString())
                progressBar?.visibility = View.GONE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.country_description_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.gps_distance) {
            activity?.showDialogWithOneButton(
                null,
                getString(R.string.distanceToYou, mCountryName, distance),
                R.string.dialog_ok,
                null
            )
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("MissingPermission")
    private fun getDistance() {
        val currentCountryLocation = Location(LocationManager.GPS_PROVIDER).apply {
            latitude = currentCountryLatLng.latitude
            longitude = currentCountryLatLng.longitude
        }
        LocationServices.getFusedLocationProviderClient(context)
            .lastLocation
            .addOnSuccessListener { location ->
                distance = location.distanceTo(currentCountryLocation).toInt() / 1000
                Log.d(LOG_TAG, location.toString())
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}