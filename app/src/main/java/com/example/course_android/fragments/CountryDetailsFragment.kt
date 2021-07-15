package com.example.course_android.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.course_android.AdapterLanguages
import com.example.course_android.Constants
import com.example.course_android.CountriesApp
import com.example.course_android.R
import com.example.course_android.api.CountryDescriptionApi
import com.example.course_android.api.RetrofitObj
import com.example.course_android.databinding.FragmentCountryDetailsBinding
import com.example.course_android.dto.CountryDetailsDtoTransformer
import com.example.course_android.dto.model.CountryDescriptionItemDto
import com.example.course_android.dto.model.LanguageOfOneCountryDto
import com.example.course_android.model.oneCountry.CountryDescriptionItem
import com.example.course_android.utils.loadSvg
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_country_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryDetailsFragment : Fragment(R.layout.fragment_country_details) {

    private lateinit var mCountryName: String
    private var binding: FragmentCountryDetailsBinding? = null
    private var mLanguageList: List<LanguageOfOneCountryDto>? = null
    lateinit var adapterLanguages: AdapterLanguages
    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var countryDescriptionFromApi: MutableList<CountryDescriptionItem>
    private lateinit var countryDetailsDto: CountryDescriptionItemDto
    private var mSrCountryDetails: SwipeRefreshLayout? = null

    private lateinit var googleMap: GoogleMap
    var mapFragment: SupportMapFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCountryName = arguments?.getString(Constants.COUNTRY_NAME_KEY) ?: Constants.ERROR
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCountryDetailsBinding.bind(view)
        binding?.mTvCountryName?.text = mCountryName
        mSrCountryDetails = binding?.srCountryDetails
        mSrCountryDetails?.setOnRefreshListener {
            getMyData()
        }
        getMyData()
    }

    private fun initMap(map: GoogleMap) {
        googleMap = map.apply {
            val startLocation = LatLng(
                countryDetailsDto.latlng[0],
                countryDetailsDto.latlng[1]
            )
                val cameraLocation = CameraUpdateFactory.newLatLngZoom(startLocation,7.0f)
                this.moveCamera(cameraLocation)
        }
    }

    private fun getMyData() {
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
                    countryDetailsDto = countryDetailsDtoTransformer.transform(countryDescriptionFromApi)

                    //языки ресайкл
                    linearLayoutManager = LinearLayoutManager(context)
                    recycler_languages.layoutManager = linearLayoutManager
                    adapterLanguages = AdapterLanguages()
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
            }

            override fun onFailure(call: Call<List<CountryDescriptionItem>?>, t: Throwable) {
                Log.d("RETROFIT_COUNTRIES", t.toString())
                mSrCountryDetails?.isRefreshing = false
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}