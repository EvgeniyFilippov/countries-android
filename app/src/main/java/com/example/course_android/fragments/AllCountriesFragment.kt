package com.example.course_android.fragments

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.course_android.Constants
import com.example.course_android.CountriesApp.Companion.adapterOfAllCountries
import com.example.course_android.CountriesApp.Companion.base
import com.example.course_android.CountriesApp.Companion.daoCountry
import com.example.course_android.CountriesApp.Companion.daoLanguage
import com.example.course_android.CountriesApp.Companion.retrofit
import com.example.course_android.R
import com.example.course_android.api.CountriesApi
import com.example.course_android.api.RetrofitObj
import com.example.course_android.databinding.FragmentSecondBinding
import com.example.course_android.ext.isOnline
import com.example.course_android.model.allCountries.CountriesDataItem
import com.example.course_android.room.CountryBaseInfoEntity
import com.example.course_android.room.LanguagesInfoEntity
import com.example.course_android.utils.convertDBdataToRetrofitModel
import com.example.course_android.utils.sortBySortStatusFromPref
import com.example.course_android.utils.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_second.*

class AllCountriesFragment : Fragment(R.layout.fragment_second) {

    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var listCountriesFromApi: MutableList<CountriesDataItem>
    private var listOfCountriesFromDB: MutableList<CountriesDataItem> = arrayListOf()
    private var binding: FragmentSecondBinding? = null
    private var sortStatus = Constants.DEFAULT_SORT_STATUS
    private lateinit var inet: MenuItem
    private val mCompositeDisposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readSortStatus()
        binding = FragmentSecondBinding.bind(view)
        recyclerView.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        setHasOptionsMenu(true)
        recyclerView.adapter = adapterOfAllCountries
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
        inet = menu.findItem(R.id.online)
        inet.isVisible = context?.isOnline() != true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sort_countries) {
            if (!item.isChecked) {
                adapterOfAllCountries.sortAndReplaceItem()
                item.setIcon(R.drawable.ic_baseline_keyboard_arrow_down_24)
                context?.toast(getString(R.string.sort_up))
                item.isChecked = true
                sortStatus = Constants.SORT_STATUS_UP
            } else {
                adapterOfAllCountries.sortDescendingAndReplaceItem()
                item.setIcon(R.drawable.ic_baseline_keyboard_arrow_up_24)
                context?.toast(getString(R.string.sort_down))
                item.isChecked = false
                sortStatus = Constants.SORT_STATUS_DOWN
            }
        }
        if (item.itemId == R.id.reset_sort) {
            showAlertDialog()
        }
        saveSortStatus()
        return super.onOptionsItemSelected(item)
    }

    private fun getMyData() {
        RetrofitObj.getOkHttp()
        val progressBar = binding?.progressBar as ProgressBar
        progressBar.visibility = ProgressBar.VISIBLE
        val countriesApi = retrofit.create(CountriesApi::class.java)
        val subscription = countriesApi.getTopHeadlines()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ response ->
                listCountriesFromApi = (response as MutableList<CountriesDataItem>)
                listCountriesFromApi.sortBySortStatusFromPref(sortStatus)

                adapterOfAllCountries.setItemClick { item ->
                    val bundle = Bundle()
                    bundle.putString(Constants.COUNTRY_NAME_KEY, item.name)
                    findNavController().navigate(
                        R.id.action_secondFragment_to_countryDetailsFragment,
                        bundle
                    )
                }
                adapterOfAllCountries.addList(
                    listCountriesFromApi
                )
                saveToDBfromApi()

                progressBar.visibility = ProgressBar.GONE;

            }, { throwable ->
                throwable.printStackTrace()
                if (!daoCountry?.getAllInfo().isNullOrEmpty()) {
                    getCountriesFromDB()
                }
                if (context?.isOnline() == false) {
                    context?.toast(getString(R.string.chek_inet))
                }
                progressBar.visibility = ProgressBar.GONE;
            })
        mCompositeDisposable.add(subscription)
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
        adapterOfAllCountries.setItemClick {
            if (context?.isOnline() == false) {
                context?.toast(getString(R.string.chek_inet))
            } else {
                getMyData()
            }
        }
        listOfCountriesFromDB = countriesFromDB.convertDBdataToRetrofitModel(
            languagesFromDB,
            listOfCountriesFromDB
        )
        listOfCountriesFromDB.sortBySortStatusFromPref(sortStatus)
        adapterOfAllCountries.repopulate(
            listOfCountriesFromDB
        )
        listOfCountriesFromDB.clear()
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

    private fun showAlertDialog() {
        val alertDialog = context?.let { MaterialAlertDialogBuilder(it) }
            ?.setTitle(getString(R.string.sort))
            ?.setMessage(getString(R.string.reset_sort))
            ?.setNegativeButton(getString(R.string.no)) { dialog, which ->
                dialog.dismiss()
            }
            ?.setPositiveButton(getString(R.string.yes)) { dialog, which ->
                dialog.dismiss()
                sortStatus = Constants.DEFAULT_INT
                saveSortStatus()
                adapterOfAllCountries.resetSorting()
            }
        alertDialog?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        mCompositeDisposable.clear()
    }

}