package com.example.course_android.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.course_android.Constants
import com.example.course_android.CountriesApp.Companion.base
import com.example.course_android.CountriesApp.Companion.daoCountry
import com.example.course_android.CountriesApp.Companion.daoLanguage
import com.example.course_android.CountriesApp.Companion.retrofit
import com.example.course_android.MyAdapter
import com.example.course_android.R
import com.example.course_android.api.CountriesApi
import com.example.course_android.api.RetrofitObj
import com.example.course_android.databinding.FragmentSecondBinding
import com.example.course_android.model.allCountries.CountriesDataItem
import com.example.course_android.room.*
import com.example.course_android.utils.convertDBdataToRetrofitModel
import com.example.course_android.utils.sortBySortStatusFromPref
import com.example.course_android.utils.toast
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_second.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SecondFragment : Fragment(R.layout.fragment_second) {

    lateinit var myAdapter: MyAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var listCountriesFromApi: MutableList<CountriesDataItem>
    private var listOfCountriesFromDB: MutableList<CountriesDataItem> = arrayListOf()
    private var binding: FragmentSecondBinding? = null

    private var sortStatus = Constants.DEFAULT_SORT_STATUS


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readSortStatus()
        binding = FragmentSecondBinding.bind(view)
        recyclerView.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        setHasOptionsMenu(true)

        myAdapter = MyAdapter()
        recyclerView.adapter = myAdapter

        if (!daoCountry?.getAllInfo().isNullOrEmpty()) {
            getCountriesFromDB()
        }
        getMyData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.countries_sort_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        if (sortStatus == Constants.SORT_STATUS_UP) {
            menu.findItem(R.id.sort_countries)
                .setIcon(R.drawable.ic_baseline_keyboard_arrow_down_24).isChecked = true
            context?.toast(getString(R.string.sort_up))
        } else if (sortStatus == Constants.SORT_STATUS_DOWN) {
            menu.findItem(R.id.sort_countries).setIcon(R.drawable.ic_baseline_keyboard_arrow_up_24)
            context?.toast(getString(R.string.sort_down))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sort_countries) {
            if (!item.isChecked) {
                myAdapter.sortAndReplaceItem()
                item.setIcon(R.drawable.ic_baseline_keyboard_arrow_down_24)
                context?.toast(getString(R.string.sort_up))
                item.isChecked = true
                sortStatus = Constants.SORT_STATUS_UP
            } else {
                myAdapter.sortDescendingAndReplaceItem()
                item.setIcon(R.drawable.ic_baseline_keyboard_arrow_up_24)
                context?.toast(getString(R.string.sort_down))
                item.isChecked = false
                sortStatus = Constants.SORT_STATUS_DOWN
            }
            saveSortStatus()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getMyData() {
        RetrofitObj.getOkHttp()
        val countriesApi = retrofit.create(CountriesApi::class.java)
        val countriesApiCall = countriesApi.getTopHeadlines()
        val progressBar = binding?.progressBar as ProgressBar
        progressBar.visibility = ProgressBar.VISIBLE
        countriesApiCall.enqueue(object : Callback<List<CountriesDataItem>?> {
            override fun onResponse(
                call: Call<List<CountriesDataItem>?>,
                response: Response<List<CountriesDataItem>?>
            ) {
                if (response.body() != null) {
                    listCountriesFromApi = (response.body() as MutableList<CountriesDataItem>)
                    listCountriesFromApi.sortBySortStatusFromPref(sortStatus)

                    myAdapter.setItemClick { item ->
                        val bundle = Bundle()
                        bundle.putString(Constants.COUNTRY_NAME_KEY, item.name)
                        findNavController().navigate(
                            R.id.action_secondFragment_to_countryDetailsFragment,
                            bundle
                        )
                    }

                    myAdapter.repopulate(listCountriesFromApi)

                    progressBar.visibility = ProgressBar.GONE;
                    saveToDBfromApi()
                } else {
                    Log.d("RETROFIT_COUNTRIES", response.body().toString())
                }
            }

            override fun onFailure(call: Call<List<CountriesDataItem>?>, t: Throwable) {
                Log.d("RETROFIT_COUNTRIES", t.toString())
            }
        })
    }

    private fun saveSortStatus() {
        activity?.getSharedPreferences(Constants.FILE_NAME_PREF, Context.MODE_PRIVATE)
            ?.edit()
            ?.apply { putInt(Constants.KEY_SORT_STATUS, sortStatus) }
            ?.apply()
    }

    private fun readSortStatus() {
        val sharedPreference =
            activity?.getSharedPreferences(Constants.FILE_NAME_PREF, Context.MODE_PRIVATE)
        val reader = sharedPreference?.getInt(Constants.KEY_SORT_STATUS, Constants.DEFAULT_INT)
        if (reader != null) {
            sortStatus = reader
        }
    }

    private fun getCountriesFromDB() {
        val countriesFromDB = base?.getCountryInfoDAO()?.getAllInfo()
        val languagesFromDB = base?.getLanguageInfoDAO()
        listOfCountriesFromDB = countriesFromDB.convertDBdataToRetrofitModel(
            languagesFromDB,
            listOfCountriesFromDB
        )
        listOfCountriesFromDB.sortBySortStatusFromPref(sortStatus)
        myAdapter.repopulate(listOfCountriesFromDB.subList(0, 20))
    }

    private fun saveToDBfromApi() {
        val listOfAllCountries: MutableList<CountryBaseInfoEntity> = mutableListOf()
        val listOfAllLanguages: MutableList<LanguagesInfoEntity> = mutableListOf()
        listCountriesFromApi.let {
            listCountriesFromApi.forEach { item ->
                listOfAllCountries.add(
                    CountryBaseInfoEntity(
                        item.name,
                        item.capital,
                        item.area
                    )
                )
                item.languages.forEach { language ->
                    listOfAllLanguages.add(
                        LanguagesInfoEntity(
                            item.name,
                            language.name
                        )
                    )
                }
            }
            daoCountry?.addAll(listOfAllCountries)
            daoLanguage?.addAll(listOfAllLanguages)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}