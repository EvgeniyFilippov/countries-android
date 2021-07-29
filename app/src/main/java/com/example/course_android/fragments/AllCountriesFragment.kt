package com.example.course_android.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.course_android.Constants
import com.example.course_android.Constants.COUNTRY_NAME_KEY
import com.example.course_android.Constants.DEFAULT_INT
import com.example.course_android.Constants.FILE_NAME_PREF
import com.example.course_android.Constants.KEY_SORT_STATUS
import com.example.course_android.CountriesApp.Companion.base
import com.example.course_android.CountriesApp.Companion.daoCountry
import com.example.course_android.CountriesApp.Companion.daoLanguage
import com.example.course_android.CountriesApp.Companion.retrofit
import com.example.course_android.R
import com.example.course_android.adapters.AdapterOfAllCountries
import com.example.course_android.api.CountriesApi
import com.example.course_android.api.RetrofitObj
import com.example.course_android.databinding.FragmentAllCountriesBinding
import com.example.course_android.dto.CountryDetailsDtoTransformer
import com.example.course_android.dto.model.CountryDescriptionItemDto
import com.example.course_android.ext.isOnline
import com.example.course_android.room.CountryBaseInfoEntity
import com.example.course_android.room.LanguagesInfoEntity
import com.example.course_android.utils.convertDBdataToRetrofitModel
import com.example.course_android.utils.sortBySortStatusFromPref
import com.example.course_android.utils.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.TestObserver.create
import io.reactivex.rxjava3.processors.PublishProcessor.create
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_all_countries.*
import java.net.URI.create
import java.util.*
import java.util.concurrent.TimeUnit

class AllCountriesFragment : Fragment(R.layout.fragment_all_countries) {

    private lateinit var listCountriesFromApiDto: MutableList<CountryDescriptionItemDto>
    private var listCountriesFromSearch: MutableList<CountryDescriptionItemDto> = arrayListOf()

    private var listOfCountriesFromDB: MutableList<CountryDescriptionItemDto> = arrayListOf()
    private var binding: FragmentAllCountriesBinding? = null
    private var sortStatus = Constants.DEFAULT_SORT_STATUS
    private lateinit var inet: MenuItem
    private val mCompositeDisposable = CompositeDisposable()
    var adapterOfAllCountries = AdapterOfAllCountries()
    private val countryDetailsDtoTransformer = CountryDetailsDtoTransformer()
    private lateinit var mSearchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readSortStatus()
        binding = FragmentAllCountriesBinding.bind(view)
        mSearchView = binding!!.appBarSearch
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapterOfAllCountries
        setHasOptionsMenu(true)
        getMyData()
        search()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.countries_sort_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
        if (sortStatus == Constants.SORT_STATUS_UP) {
            menu.findItem(R.id.sort_countries)
                .setIcon(R.drawable.ic_baseline_keyboard_arrow_down_24).isChecked = true
        } else if (sortStatus == Constants.SORT_STATUS_DOWN) {
            menu.findItem(R.id.sort_countries).setIcon(R.drawable.ic_baseline_keyboard_arrow_up_24)
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
            .doOnNext { list ->
                listCountriesFromApiDto = countryDetailsDtoTransformer.transform(list)
                listCountriesFromApiDto.sortBySortStatusFromPref(sortStatus)
                saveToDBfromApi()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ _ ->
                adapterOfAllCountries.repopulate(
                    listCountriesFromApiDto
                )
                adapterOfAllCountries.setItemClick { item ->
                    val bundle = Bundle()
                    bundle.putString(COUNTRY_NAME_KEY, item.name)
                    findNavController().navigate(
                        R.id.action_secondFragment_to_countryDetailsFragment,
                        bundle
                    )
                }
                progressBar.visibility = ProgressBar.GONE;
            }, { throwable ->
                throwable.printStackTrace()
                getCountriesFromDB()
                if (context?.isOnline() == false) {
                    context?.toast(getString(R.string.chek_inet))
                }
                progressBar.visibility = ProgressBar.GONE;
            })
        mCompositeDisposable.add(subscription)
    }

    private fun saveSortStatus() {
        activity?.getSharedPreferences(FILE_NAME_PREF, Context.MODE_PRIVATE)
            ?.edit()
            ?.apply { putInt(KEY_SORT_STATUS, sortStatus) }
            ?.apply()
    }

    private fun readSortStatus() {
        val sharedPreference =
            activity?.getSharedPreferences(FILE_NAME_PREF, Context.MODE_PRIVATE)
        val reader = sharedPreference?.getInt(KEY_SORT_STATUS, DEFAULT_INT)
        if (reader != null) {
            sortStatus = reader
        }
    }

    private fun getCountriesFromDB() {
        val countriesFromDB = base?.getCountryInfoDAO()?.getAllInfo()
        val languagesFromDB = base?.getLanguageInfoDAO()
        val subscription = countriesFromDB
            ?.doOnNext { list ->
                listOfCountriesFromDB = list.convertDBdataToRetrofitModel(
                    languagesFromDB,
                    listOfCountriesFromDB
                )
                listOfCountriesFromDB.sortBySortStatusFromPref(sortStatus)
            }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                adapterOfAllCountries.repopulate(
                    listOfCountriesFromDB
                )
                listOfCountriesFromDB.clear()
                adapterOfAllCountries.setItemClick {
                    if (context?.isOnline() == false) {
                        context?.toast(getString(R.string.chek_inet))
                    } else {
                        getMyData()
                    }
                }
            }, { throwable ->
                throwable.printStackTrace()
            })
        mCompositeDisposable.add(subscription)
    }

    private fun saveToDBfromApi() {
        val listOfAllCountries: MutableList<CountryBaseInfoEntity> = mutableListOf()
        val listOfAllLanguages: MutableList<LanguagesInfoEntity> = mutableListOf()
        listCountriesFromApiDto.let {
            listCountriesFromApiDto.forEach { item ->
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
            ?.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            ?.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                dialog.dismiss()
                sortStatus = DEFAULT_INT
                saveSortStatus()
                adapterOfAllCountries.resetSorting()
            }
        alertDialog?.show()
    }

    private fun search() {
        Observable.create(ObservableOnSubscribe<String> { subscriber ->
            mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    subscriber.onNext(newText!!)
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    subscriber.onNext(query!!)
                    return false
                }
            })
        })
            .map { text -> text.toLowerCase().trim() }
            .debounce(10, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
//            .filter { text -> text.length >=3 }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { text ->
                Log.d(TAG, "subscriber: $text")
                searchInAdapter(text)
            }
    }

    private fun searchInAdapter(text: String) {
        listCountriesFromSearch.clear()
        if (text.length >= 3) {
            listCountriesFromApiDto.forEach { country ->
                if (country.name.contains(text, ignoreCase = true)) {
                    listCountriesFromSearch.add(country)
                }
            }
            adapterOfAllCountries.repopulate(
                listCountriesFromSearch
            )
        } else {
            adapterOfAllCountries.repopulate(
                listCountriesFromApiDto
            )
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        mCompositeDisposable.clear()
    }

}