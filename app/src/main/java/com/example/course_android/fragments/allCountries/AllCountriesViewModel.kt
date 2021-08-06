package com.example.course_android.fragments.allCountries

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.course_android.Constants.DEBOUNCE_TIME_MILLIS
import com.example.course_android.Constants.END_AREA_FILTER_KEY
import com.example.course_android.Constants.END_DISTANCE_FILTER_KEY
import com.example.course_android.Constants.END_POPULATION_FILTER_KEY
import com.example.course_android.Constants.MIN_SEARCH_STRING_LENGTH
import com.example.course_android.Constants.START_AREA_FILTER_KEY
import com.example.course_android.Constants.START_DISTANCE_FILTER_KEY
import com.example.course_android.Constants.START_POPULATION_FILTER_KEY
import com.example.course_android.CountriesApp
import com.example.course_android.api.RetrofitObj
import com.example.course_android.base.mvvm.*
import com.example.course_android.dto.model.CountryDescriptionItemDto
import com.example.course_android.dto.transformCountryToDto
import com.example.course_android.room.CountryBaseInfoEntity
import com.example.course_android.room.LanguagesInfoEntity
import com.example.course_android.utils.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class AllCountriesViewModel(
    private val sortStatus: Int,
    private val mSearchSubject: BehaviorSubject<String>,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(savedStateHandle) {

    val mutableCountriesLiveData =
        MutableLiveData<Outcome<MutableList<CountryDescriptionItemDto>>>()
    val mutableCountriesFromSearchLiveData =
        SingleLiveEvent<Outcome<MutableList<CountryDescriptionItemDto>>>()

    private var listOfCountriesFromDB: MutableList<CountryDescriptionItemDto> = arrayListOf()
    private var listCountriesFromFilter: MutableList<CountryDescriptionItemDto> = arrayListOf()

    fun getCountriesFromApi() {
        RetrofitObj.getCountriesApi().getListOfCountry()
            .map { it.transformCountryToDto() }
            .doOnNext { listDto -> listDto.sortBySortStatusFromPref(sortStatus) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mutableCountriesLiveData.next(it)
                saveToDBfromApi(it)
            }, {
                mutableCountriesLiveData.failed(it)
                getCountriesFromDB()
            }, {
                if (mutableCountriesLiveData.value is Outcome.Next) {
                    mutableCountriesLiveData.success((mutableCountriesLiveData.value as Outcome.Next).data)
                }
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
            ?.subscribe({
                mutableCountriesLiveData.next(it)
                it.clear()
            }, {
                mutableCountriesLiveData.failed(it)
            }, {
                if (mutableCountriesLiveData.value is Outcome.Next) {
                    mutableCountriesLiveData.success((mutableCountriesLiveData.value as Outcome.Next).data)
                }
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

    fun getSearchSubject() {
        mCompositeDisposable.add(
            executeJob(
                mSearchSubject.toFlowable(BackpressureStrategy.LATEST)
                    .onErrorResumeNext { Flowable.just("") }
                    .filter { it.length >= MIN_SEARCH_STRING_LENGTH }
                    .debounce(DEBOUNCE_TIME_MILLIS, TimeUnit.MILLISECONDS)
                    .distinctUntilChanged()
                    .map { it.trim() }
                    .flatMap { text: String ->
                        RetrofitObj.getCountriesApi().getCountryDetails(text)

                            .map { it.transformCountryToDto() }
                            .map {
                                it.filter { country ->
                                    country.name.contains(text, true)
                                }
                                    .toMutableList()
                            }
                    }, mutableCountriesFromSearchLiveData)
        )
    }

    fun getCountriesFromFilter(mapSettingsByFilter: HashMap<String?, Int>) {
        val currentLocationOfUser = getResultOfCurrentLocation()
        RetrofitObj.getCountriesApi().getListOfCountry()
            .map { it.transformCountryToDto() }
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
                            if (country.population >= mapSettingsByFilter[START_POPULATION_FILTER_KEY] ?: 0
                                && country.population <= mapSettingsByFilter[END_POPULATION_FILTER_KEY] ?: 0
                            ) {
                                listCountriesFromFilter.add(country)
                            }
                        }
                    }
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mutableCountriesFromSearchLiveData.next(listCountriesFromFilter)
            }, {
                mutableCountriesFromSearchLiveData.failed(it)
            }, {
                if (mutableCountriesFromSearchLiveData.value is Outcome.Next) {
                    mutableCountriesFromSearchLiveData.success((mutableCountriesFromSearchLiveData.value as Outcome.Next).data)
                }
            }).also { mCompositeDisposable.add(it) }
    }
}