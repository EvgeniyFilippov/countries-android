package com.example.course_android.fragments.allCountries

import android.content.ContentValues
import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.course_android.Constants.DEBOUNCE_TIME_MILLIS
import com.example.course_android.Constants.DEFAULT_STRING
import com.example.course_android.Constants.END_AREA_FILTER_KEY
import com.example.course_android.Constants.END_DISTANCE_FILTER_KEY
import com.example.course_android.Constants.MIN_SEARCH_STRING_LENGTH
import com.example.course_android.Constants.START_AREA_FILTER_KEY
import com.example.course_android.Constants.START_DISTANCE_FILTER_KEY
import com.example.course_android.CountriesApp
import com.example.course_android.api.RetrofitObj
import com.example.course_android.base.mvvm.BaseViewModel
import com.example.course_android.dto.CountryDetailsDtoTransformer
import com.example.course_android.dto.FilterSettingsToDtoTransformer
import com.example.course_android.dto.model.CountryDescriptionItemDto
import com.example.course_android.model.oneCountry.CountryDescriptionItem
import com.example.course_android.room.CountryBaseInfoEntity
import com.example.course_android.room.LanguagesInfoEntity
import com.example.course_android.utils.*
import com.example.course_android.utils.getDistanceForFilter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class AllCountriesViewModel(
    private val sortStatus: Int,
    private val mSearchSubject: PublishSubject<String>
) : BaseViewModel() {

    val mutableCountriesLiveData = MutableLiveData<MutableList<CountryDescriptionItemDto>>()
    val mutableCountriesFromSearchLiveData =
        SingleLiveEvent<MutableList<CountryDescriptionItemDto>>()

    private var listOfCountriesFromDB: MutableList<CountryDescriptionItemDto> = arrayListOf()
    private val countryDetailsDtoTransformer = CountryDetailsDtoTransformer()

    private var searchText: String = DEFAULT_STRING

    private var listCountriesFromSearch: MutableList<CountryDescriptionItem> = arrayListOf()
    private var listCountriesFromFilter: MutableList<CountryDescriptionItemDto> = arrayListOf()

    fun getCountriesFromApi() {
        RetrofitObj.getCountriesApi().getListOfCountry()
            .map { list -> countryDetailsDtoTransformer.transform(list) }
            .doOnNext { listDto -> listDto.sortBySortStatusFromPref(sortStatus) }
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
            ?.map { list ->
                list.convertDBdataToRetrofitModel(
                    languagesFromDB,
                    listOfCountriesFromDB
                )
            }
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
            .doOnNext { list ->
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
                mutableCountriesFromSearchLiveData.value =
                    countryDetailsDtoTransformer.transform(listCountriesFromSearch)
            }, {
                Log.d(ContentValues.TAG, ("Error"))
            }).also { mCompositeDisposable.add(it) }

    fun getCountriesFromFilter(mapSettingsByFilter: HashMap<String?, Int>) {
        val currentLocationOfUser = getResultOfCurrentLocation()
        RetrofitObj.getCountriesApi().getListOfCountry()
            .map { list -> countryDetailsDtoTransformer.transform(list) }
            .doOnNext { list ->
                listCountriesFromFilter.clear()
                list.forEach { country ->
                    if (country.area >= mapSettingsByFilter[START_AREA_FILTER_KEY] ?: 0
                        && country.area <= mapSettingsByFilter[END_AREA_FILTER_KEY] ?: 0
                    ) {
                        val distance = calculateDistanceFiler(currentLocationOfUser, country)
                        if (distance >= mapSettingsByFilter[START_DISTANCE_FILTER_KEY] ?: 0 &&
                            distance <= mapSettingsByFilter[END_DISTANCE_FILTER_KEY] ?: 0
                        ) {
                            listCountriesFromFilter.add(country)
                        }
                    }
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ sortedListDto ->
                mutableCountriesFromSearchLiveData.value = listCountriesFromFilter
                saveToDBfromApi(sortedListDto)
            }, {

            }).also { mCompositeDisposable.add(it) }
    }
}

