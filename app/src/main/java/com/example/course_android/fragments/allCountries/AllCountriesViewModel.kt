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
import com.example.course_android.base.mvvm.*
import com.example.course_android.dto.model.CountryDescriptionItemDto
import com.example.course_android.room.CountryBaseInfoEntity
import com.example.course_android.room.LanguagesInfoEntity
import com.example.course_android.utils.*
import com.repository.database.DatabaseCountryRepository
import com.repository.database.DatabaseLanguageRepository
import com.repository.network.NetworkRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class AllCountriesViewModel(
    savedStateHandle: SavedStateHandle,
    private val mDatabaseCountryRepository: DatabaseCountryRepository,
    private val mDatabaseLanguageRepository: DatabaseLanguageRepository,
//    private val sortStatus: Int,
//    private val mSearchSubject: BehaviorSubject<String>,
//    private val mDatabaseRepository: DatabaseLanguageRepository,
    private val mNetworkRepository: NetworkRepository
) : BaseViewModel(savedStateHandle) {

    private val mSearchSubject = BehaviorSubject.create<String>()

    val allCountriesLiveData =
        MutableLiveData<Outcome<MutableList<CountryDescriptionItemDto>>>()
    val countriesFromSearchAndFilterLiveData =
        SingleLiveEvent<Outcome<MutableList<CountryDescriptionItemDto>>>()

    private var listOfCountriesFromDB: MutableList<CountryDescriptionItemDto> = arrayListOf()
    private var listCountriesFromFilter: MutableList<CountryDescriptionItemDto> = arrayListOf()

    fun getCountriesFromApi() {
        mNetworkRepository.getListOfCountry()
//            .map { it -> it.sortBySortStatusFromPref(sortStatus) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                allCountriesLiveData.next(it)
                saveToDBfromApi(it)
            }, {
                allCountriesLiveData.failed(it)
                getCountriesFromDB()
            }, {
                if (allCountriesLiveData.value is Outcome.Next) {
                    allCountriesLiveData.success((allCountriesLiveData.value as Outcome.Next).data)
                }
            }).also { mCompositeDisposable.add(it) }
    }


    private fun getCountriesFromDB() {
        val countriesFromDB = mDatabaseCountryRepository.getAllInfo()
        val languagesFromDB = mDatabaseLanguageRepository
        countriesFromDB
            .map { list ->
                list.convertDBdataToRetrofitModel(
                    languagesFromDB,
                    listOfCountriesFromDB
                )
            }
//            ?.map { it -> it.sortBySortStatusFromPref(sortStatus) }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                allCountriesLiveData.next(it)
                it.clear()
            }, {
                allCountriesLiveData.failed(it)
            }, {
                if (allCountriesLiveData.value is Outcome.Next) {
                    allCountriesLiveData.success((allCountriesLiveData.value as Outcome.Next).data)
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
            mDatabaseCountryRepository.addAll(listOfAllCountries)
            mDatabaseLanguageRepository.addAll(listOfAllLanguages)
        }
    }

    fun getCountriesFromSearch() {
        mCompositeDisposable.add(
            executeJob(
                mSearchSubject.toFlowable(BackpressureStrategy.LATEST)
                    .onErrorResumeNext { Flowable.just("") }
                    .filter { it.length >= MIN_SEARCH_STRING_LENGTH }
                    .debounce(DEBOUNCE_TIME_MILLIS, TimeUnit.MILLISECONDS)
                    .distinctUntilChanged()
                    .map { it.trim() }
                    .flatMap { text: String ->
                        mNetworkRepository.getCountryDetails(text)
                            .map {
                                it.filter { country ->
                                    country.name.contains(text, true)
                                }
                                    .toMutableList()
                            }
                    }, countriesFromSearchAndFilterLiveData)
        )
    }

    fun getCountriesFromFilter(mapSettingsByFilter: HashMap<String?, Int>) {
        val currentLocationOfUser = getResultOfCurrentLocation()
        mNetworkRepository.getListOfCountry()
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
                countriesFromSearchAndFilterLiveData.next(listCountriesFromFilter)
            }, {
                countriesFromSearchAndFilterLiveData.failed(it)
            }, {
                if (countriesFromSearchAndFilterLiveData.value is Outcome.Next) {
                    countriesFromSearchAndFilterLiveData.success((countriesFromSearchAndFilterLiveData.value as Outcome.Next).data)
                }
            }).also { mCompositeDisposable.add(it) }
    }

    fun getSearchSubject(): BehaviorSubject<String> = mSearchSubject
}