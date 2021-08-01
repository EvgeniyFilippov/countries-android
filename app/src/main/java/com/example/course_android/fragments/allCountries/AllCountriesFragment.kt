package com.example.course_android.fragments.allCountries

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.course_android.Constants
import com.example.course_android.Constants.COUNTRY_NAME_KEY
import com.example.course_android.Constants.DEBOUNCE_TIME_MILLIS
import com.example.course_android.Constants.DEFAULT_INT
import com.example.course_android.Constants.DEFAULT_SORT_STATUS
import com.example.course_android.Constants.FILE_NAME_PREF
import com.example.course_android.Constants.KEY_SORT_STATUS
import com.example.course_android.Constants.MIN_SEARCH_STRING_LENGTH
import com.example.course_android.CountriesApp.Companion.base
import com.example.course_android.CountriesApp.Companion.daoCountry
import com.example.course_android.CountriesApp.Companion.daoLanguage
import com.example.course_android.R
import com.example.course_android.adapters.AdapterOfAllCountries
import com.example.course_android.api.RetrofitObj
import com.example.course_android.base.mvvm.BaseMvvmView
import com.example.course_android.databinding.FragmentAllCountriesBinding
import com.example.course_android.dto.CountryDetailsDtoTransformer
import com.example.course_android.dto.model.CountryDescriptionItemDto
import com.example.course_android.ext.isOnline
import com.example.course_android.model.oneCountry.CountryDescriptionItem
import com.example.course_android.room.CountryBaseInfoEntity
import com.example.course_android.room.LanguagesInfoEntity
import com.example.course_android.utils.convertDBdataToRetrofitModel
import com.example.course_android.utils.sortBySortStatusFromPref
import com.example.course_android.utils.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.fragment_all_countries.*
import java.util.concurrent.TimeUnit

class AllCountriesFragment : Fragment(R.layout.fragment_all_countries), BaseMvvmView {

//    private lateinit var listCountriesFromApiDto: MutableList<CountryDescriptionItemDto>
    private var listCountriesFromSearch: MutableList<CountryDescriptionItem> = arrayListOf()

    private var listOfCountriesFromDB: MutableList<CountryDescriptionItemDto> = arrayListOf()
    private var binding: FragmentAllCountriesBinding? = null
    private var sortStatus = DEFAULT_SORT_STATUS
    private lateinit var inet: MenuItem
    private val mCompositeDisposable = CompositeDisposable()
    var adapterOfAllCountries = AdapterOfAllCountries()

    private val mSearchSubject = BehaviorSubject.create<String>()
    private var searchText: String = ""

    private lateinit var viewModel: AllCountriesViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readSortStatus()
        binding = FragmentAllCountriesBinding.bind(view)

        viewModel = ViewModelProvider(this, AllCountriesViewModelFactory(sortStatus))
            .get(AllCountriesViewModel::class.java)
            .also {
                it.countriesLiveData.observe(viewLifecycleOwner, Observer { data -> showCountryFromApi(data) })
                it.countriesErrorLiveData.observe(viewLifecycleOwner, Observer { error -> showError(error) })
                it.getCountriesFromApi()
            }

        with(viewModel) {

        }

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapterOfAllCountries
        setHasOptionsMenu(true)
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

        getSearchSubject()

        val menuSearchItem = menu.findItem(R.id.menu_search_button)
        val mSvMenu: SearchView = menuSearchItem.actionView as SearchView
        mSvMenu.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { mSearchSubject.onNext(query) }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mSearchSubject.onNext(newText)
                return true
            }
        })

        mSvMenu.setOnCloseListener {
            listCountriesFromApiDto.sortBySortStatusFromPref(sortStatus)
            adapterOfAllCountries.repopulate(listCountriesFromApiDto)
            false
        }
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
            showSortResetDialog()
        }
        saveSortStatus()
        return super.onOptionsItemSelected(item)
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



    private fun showCountryFromApi( listCountriesFromApiDto: MutableList<CountryDescriptionItemDto>) {
        adapterOfAllCountries.repopulate(
            listCountriesFromApiDto
        )
        adapterOfAllCountries.setItemClick { item ->
            val bundle = Bundle()
            bundle.putString(Constants.COUNTRY_NAME_KEY, item.name)
            findNavController().navigate(
                R.id.action_secondFragment_to_countryDetailsFragment,
                bundle
            )
        }
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

    private fun showSortResetDialog() {
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

    private fun getSearchSubject(): Disposable =
        mSearchSubject
            .filter { it.length >= MIN_SEARCH_STRING_LENGTH }
            .debounce(DEBOUNCE_TIME_MILLIS, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .map { it.trim() }
            .doOnNext { searchText = it }
            .flatMap { text ->
                RetrofitObj.getCountriesApi().getCountryDetails(text).toObservable()
                    .onErrorResumeNext { Observable.just(mutableListOf()) }
            }
            .doOnNext { list ->
                listCountriesFromSearch.clear()
                list.forEach { country ->
                    if (country.name?.contains(searchText, ignoreCase = true) == true) {
                        listCountriesFromSearch.add(country)
                    }
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                adapterOfAllCountries.repopulate(
                    countryDetailsDtoTransformer.transform(
                        listCountriesFromSearch
                    )
                )
            }, {
                Log.d(TAG, ("Error"))
            }).also { mCompositeDisposable.add(it) }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        mCompositeDisposable.clear()
    }

    override fun showError(error: String) {
        TODO("Not yet implemented")
    }

    override fun showProgress() {
        TODO("Not yet implemented")
    }

    override fun hideProgress() {
        TODO("Not yet implemented")
    }

}