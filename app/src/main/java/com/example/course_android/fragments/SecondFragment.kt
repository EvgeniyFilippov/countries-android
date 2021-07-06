package com.example.course_android.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.course_android.MyAdapter
import com.example.course_android.R
import com.example.course_android.api.CountriesApi
import com.example.course_android.databinding.FragmentSecondBinding
import com.example.course_android.model.CountriesDataItem
import com.example.course_android.room.*
import com.example.course_android.utils.toast
import kotlinx.android.synthetic.main.fragment_second.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SecondFragment : Fragment(R.layout.fragment_second) {

    lateinit var myAdapter: MyAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var responseBody: MutableList<CountriesDataItem>
    private var binding: FragmentSecondBinding? = null
    private val okHttpClientBuilder = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
    private val logging = HttpLoggingInterceptor()
    private var sortStatus = 0

    private lateinit var country: String

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://restcountries.eu/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientBuilder.build())
            .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readData()
        binding = FragmentSecondBinding.bind(view)
        recyclerView.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        setHasOptionsMenu(true)
        val base = context?.let { DatabaseInfo.init(it) }
        val daoCountry = base?.getCountryInfoDAO()
        val daoLanguage = base?.getLanguageInfoDao()
        getMyData(daoCountry, daoLanguage)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.countries_sort_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        if (sortStatus == 1) {
            menu.findItem(R.id.sort_countries).setIcon(R.drawable.ic_baseline_keyboard_arrow_down_24).isChecked = true
            context?.toast(getString(R.string.sort_up))
        } else if (sortStatus == 2) {
            menu.findItem(R.id.sort_countries).setIcon(R.drawable.ic_baseline_keyboard_arrow_up_24)
            context?.toast(getString(R.string.sort_down))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sort_countries) {
            if (!item.isChecked) {
                responseBody.sortBy { it.area }
                item.setIcon(R.drawable.ic_baseline_keyboard_arrow_down_24)
                context?.toast(getString(R.string.sort_up))
                item.isChecked = true
                sortStatus = 1
            } else {
                responseBody.sortByDescending { it.area }
                item.setIcon(R.drawable.ic_baseline_keyboard_arrow_up_24)
                context?.toast(getString(R.string.sort_down))
                item.isChecked = false
                sortStatus = 2

            }
            myAdapter.notifyDataSetChanged()
            saveData()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getMyData(daoCounty: CountryInfoDAO?, daoLanguage: LanguagesInfoDao?) {
        logging.level = HttpLoggingInterceptor.Level.BODY
        okHttpClientBuilder.addInterceptor(logging)
        val countriesApi = retrofit.create(CountriesApi::class.java)
        val countriesApiCall = countriesApi.getTopHeadlines()

        countriesApiCall.enqueue(object : Callback<List<CountriesDataItem>?> {
            override fun onResponse(
                call: Call<List<CountriesDataItem>?>,
                response: Response<List<CountriesDataItem>?>
            ) {
                if (response.body() != null) {
                    responseBody = (response.body() as MutableList<CountriesDataItem>)
                    responseBody.forEach{
                        daoCounty?.add(CountryBaseInfoEntity(it.name, it.capital, it.area))
                        country = it.name
                        it.languages.forEach{ language ->
                            daoLanguage?.add(LanguagesInfoEntity(id, country, language.name))
                        }
                    }
                    if (sortStatus == 1 ) {
                        responseBody.sortBy { it.area }
                    } else if (sortStatus == 2) {
                        responseBody.sortByDescending { it.area }
                    }
                    myAdapter = MyAdapter(this, responseBody)
                    recyclerView.adapter = myAdapter

                } else {
                    Log.d("RETROFIT_COUNTRIES", response.body().toString())
                }
            }

            override fun onFailure(call: Call<List<CountriesDataItem>?>, t: Throwable) {
                Log.d("RETROFIT_COUNTRIES", t.toString())
            }
        })
    }

    private fun saveData() {
        activity?.getSharedPreferences(FILE_NAME_PREF, Context.MODE_PRIVATE)
            ?.edit()
            ?.apply { putInt(KEY_SORT_STATUS, sortStatus) }
            ?.apply()
    }

    private fun readData() {
        val sharedPreference = activity?.getSharedPreferences(FILE_NAME_PREF, Context.MODE_PRIVATE)
        val reader = sharedPreference?.getInt(KEY_SORT_STATUS, DEFAULT_INT)
        if (reader != null) {
            sortStatus = reader
        }
    }

    companion object {
        private const val FILE_NAME_PREF = "data"
        private const val KEY_SORT_STATUS = "sortStatus"
        private const val DEFAULT_INT = 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}