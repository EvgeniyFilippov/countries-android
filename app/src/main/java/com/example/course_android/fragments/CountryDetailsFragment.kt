package com.example.course_android.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
//import coil.ImageLoader
//import coil.decode.SvgDecoder
//import coil.request.ImageRequest
import com.example.course_android.AdapterLanguages
import com.example.course_android.Constants
import com.example.course_android.CountriesApp
import com.example.course_android.R
import com.example.course_android.api.CountryDescriptionApi
import com.example.course_android.api.RetrofitObj
import com.example.course_android.databinding.FragmentCountryDetailsBinding
import com.example.course_android.model.allCountries.Language
import com.example.course_android.model.oneCountry.CountryDescriptionItem
import com.example.course_android.model.oneCountry.LanguageOfOneCountry
import com.google.android.libraries.maps.CameraUpdateFactory
//import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.model.LatLng
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_country_details.*
import kotlinx.android.synthetic.main.fragment_second.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CountryDetailsFragment : Fragment() {

    private lateinit var mCountryName: String
    private lateinit var mCountryFlagString: String
    private lateinit var mLanguageJsonString: String
    private lateinit var binding: FragmentCountryDetailsBinding
    private var mLanguageList: List<LanguageOfOneCountry>? = null
    lateinit var adapterLanguages: AdapterLanguages
    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var countryDescriptionFromApi: MutableList<CountryDescriptionItem>

    private lateinit var googleMap: GoogleMap
    var mapFragment: SupportMapFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCountryName = arguments?.getString(Constants.COUNTRY_NAME_KEY) ?: Constants.ERROR


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCountryDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMyData()
        //название страны
        binding.mTvCountryName.text = mCountryName



        //карта гугл
        mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragmentContainer) as? SupportMapFragment?
        mapFragment?.run {
            getMapAsync { map -> initMap(map) }
        }




    }


    private fun initMap(map: GoogleMap) {
        googleMap = map.apply {
            val startLocation = LatLng(53.0, 28.0)
            val cameraLocation = CameraUpdateFactory.newLatLngZoom(startLocation, 5.0f)
            this.moveCamera(cameraLocation)
        }

    }

    private fun AppCompatImageView.loadSvg(url: String) {
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
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

                    //языки ресайкл
                    linearLayoutManager = LinearLayoutManager(context)
                    recycler_languages.layoutManager = linearLayoutManager
                    adapterLanguages = AdapterLanguages()

                    mLanguageList = countryDescriptionFromApi[0].languages

                    val params: ViewGroup.LayoutParams = recycler_languages.layoutParams
                    params.height =
                        mLanguageList?.size?.times(Constants.LANGUAGE_VIEW_HEIGHT) ?: Constants.DEFAULT_INT
                    recycler_languages.layoutParams = params

                    recycler_languages.adapter = adapterLanguages
                    adapterLanguages.repopulate(mLanguageList as MutableList<LanguageOfOneCountry>)

                    //флаг
                    binding.itemFlag.loadSvg(countryDescriptionFromApi[0].flag)



                } else {
                    Log.d("RETROFIT_COUNTRIES", response.body().toString())
                }

            }

            override fun onFailure(call: Call<List<CountryDescriptionItem>?>, t: Throwable) {
                Log.d("RETROFIT_COUNTRIES", t.toString())
            }
        })
    }
}