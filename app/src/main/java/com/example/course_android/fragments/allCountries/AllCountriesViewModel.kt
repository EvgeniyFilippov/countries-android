package com.example.course_android.fragments.allCountries

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.course_android.Constants
import com.example.course_android.Constants.DEBOUNCE_TIME_MILLIS
import com.example.course_android.Constants.DEFAULT_STRING
import com.example.course_android.Constants.MIN_SEARCH_STRING_LENGTH
import com.example.course_android.CountriesApp
import com.example.course_android.R
import com.example.course_android.api.RetrofitObj
import com.example.course_android.base.mvvm.BaseViewModel
import com.example.course_android.databinding.FragmentAllCountriesBinding
import com.example.course_android.dto.CountryDetailsDtoTransformer
import com.example.course_android.dto.model.CountryDescriptionItemDto
import com.example.course_android.model.oneCountry.CountryDescriptionItem
import com.example.course_android.room.CountryBaseInfoEntity
import com.example.course_android.room.LanguagesInfoEntity
import com.example.course_android.utils.convertDBdataToRetrofitModel
import com.example.course_android.utils.sortBySortStatusFromPref
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class AllCountriesViewModel(
    private val sortStatus: Int,
    private val mSearchSubject: BehaviorSubject<String>
    ) : BaseViewModel() {

    private val mutableCountriesLiveData = MutableLiveData<MutableList<CountryDescriptionItemDto>>()
    val countriesLiveData: LiveData<MutableList<CountryDescriptionItemDto>> =
        mutableCountriesLiveData

    private val mutableCountriesErrorLiveData = MutableLiveData<String>()
    val countriesErrorLiveData: LiveData<String> = mutableCountriesErrorLiveData

    private val mutableCountriesFromSearchLiveData = MutableLiveData<MutableList<CountryDescriptionItemDto>>()
    val countriesFromSearchLiveData: LiveData<MutableList<CountryDescriptionItemDto>> = mutableCountriesFromSearchLiveData

    private var listOfCountriesFromDB: MutableList<CountryDescriptionItemDto> = arrayListOf()
    private val countryDetailsDtoTransformer = CountryDetailsDtoTransformer()

    private var searchText: String = DEFAULT_STRING

    private var listCountriesFromSearch: MutableList<CountryDescriptionItem> = arrayListOf()

    fun getCountriesFromApi() {
        RetrofitObj.getCountriesApi().getListOfCountry()
            .map { list -> countryDetailsDtoTransformer.transform(list) }
            .doOnNext { listDto -> listDto.sortBySortStatusFromPref(sortStatus)}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ sortedListDto ->
                mutableCountriesLiveData.value = sortedListDto
                saveToDBfromApi(sortedListDto)
            }, {
                getCountriesFromDB()
//                if (cone?.isOnline() == false) {
//                    mutableCountriesErrorLiveData.value = "Error"
//                    context?.toast(getString(R.string.chek_inet))
//                }
            }).also { mCompositeDisposable.add(it) }
    }

    private fun getCountriesFromDB() {
        val countriesFromDB = CountriesApp.base?.getCountryInfoDAO()?.getAllInfo()
        val languagesFromDB = CountriesApp.base?.getLanguageInfoDAO()
        countriesFromDB
            ?.map { list -> list.convertDBdataToRetrofitModel(languagesFromDB,
                listOfCountriesFromDB) }
            ?.doOnNext { list -> list.sortBySortStatusFromPref(sortStatus) }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ sortedListDto ->
                mutableCountriesLiveData.value = sortedListDto
                sortedListDto.clear()
            }, { throwable ->
                throwable.printStackTrace()
            }).also { mCompositeDisposable.add(it) }
    }

    private fun saveToDBfromApi(listCountriesFromApiDto: MutableList<CountryDescriptionItemDto>) {
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
            CountriesApp.daoCountry?.addAll(listOfAllCountries)
            CountriesApp.daoLanguage?.addAll(listOfAllLanguages)
        }
    }

    fun getSearchSubject(): Disposable =
        mSearchSubject
            .filter { it.length >= MIN_SEARCH_STRING_LENGTH }
            .debounce(DEBOUNCE_TIME_MILLIS, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .map { it.trim() }
            .doOnNext { searchText = it }
            .flatMap { text: String ->
                RetrofitObj.getCountriesApi().getCountryDetails(text).toObservable()
                    .onErrorResumeNext { Observable.just(mutableListOf()) }
            }
            .doOnNext {  list: MutableList<CountryDescriptionItem> ->
                listCountriesFromSearch.clear()
                list.forEach { country ->
                    if (country.name?.contains(searchText, ignoreCase = true) == true) {
                        listCountriesFromSearch.add(country)
                    }
                }
                countryDetailsDtoTransformer.transform(
                    listCountriesFromSearch
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mutableCountriesFromSearchLiveData.value = countryDetailsDtoTransformer.transform(listCountriesFromSearch)
            }, {
                Log.d(ContentValues.TAG, ("Error"))
            }).also { mCompositeDisposable.add(it) }

}