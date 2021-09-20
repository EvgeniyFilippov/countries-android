package com.example.course_android.fragments.allCountries

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.example.course_android.Constants.ALL_COUNTRIES_LIVE_DATA
import com.example.course_android.Constants.DEBOUNCE_TIME_MILLIS
import com.example.course_android.Constants.DEFAULT_KM
import com.example.course_android.Constants.END_AREA_FILTER_KEY
import com.example.course_android.Constants.END_DISTANCE_FILTER_KEY
import com.example.course_android.Constants.END_POPULATION_FILTER_KEY
import com.example.course_android.Constants.MIN_SEARCH_STRING_LENGTH
import com.example.course_android.Constants.N_A
import com.example.course_android.Constants.START_AREA_FILTER_KEY
import com.example.course_android.Constants.START_DISTANCE_FILTER_KEY
import com.example.course_android.Constants.START_POPULATION_FILTER_KEY
import com.example.course_android.base.mvvm.*
import com.example.course_android.services.LocationTrackingService.Companion.defaultLocation
import com.example.course_android.utils.*
import com.example.domain.dto.model.CountryDescriptionItemDto
import com.example.domain.dto.room.RoomCountryDescriptionItemDto
import com.example.domain.dto.room.RoomLanguageOfOneCountryDto
import com.example.domain.outcome.Outcome
import com.example.domain.repository.DatabaseCountryRepository
import com.example.domain.repository.DatabaseLanguageRepository
import com.example.domain.usecase.impl.GetAllCountriesUseCase
import com.example.domain.usecase.impl.GetCountryListByNameUseCase
import com.example.domain.usecase.impl.GetListCountriesFromDbUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.koin.core.logger.KOIN_TAG
import java.util.concurrent.TimeUnit

class AllCountriesViewModel(
    savedStateHandle: SavedStateHandle,
    private val mDatabaseCountryRepository: DatabaseCountryRepository,
    private val mDatabaseLanguageRepository: DatabaseLanguageRepository,
    private val mGetAllCountriesUseCase: GetAllCountriesUseCase,
    private val mGetCountryListByNameUseCase: GetCountryListByNameUseCase,
    private val mGetListCountriesFromDbUseCase: GetListCountriesFromDbUseCase

) : BaseViewModel(savedStateHandle) {

    private var sortStatus: Int = 0
    private val mSearchSubject = BehaviorSubject.create<String>()
    val allCountriesLiveData =
        savedStateHandle.getLiveData<Outcome<MutableList<CountryDescriptionItemDto>>>(
            ALL_COUNTRIES_LIVE_DATA
        )
    val countriesFromSearchAndFilterLiveData =
        SingleLiveEvent<Outcome<MutableList<CountryDescriptionItemDto>>>()

    private var listOfCountriesFromDB: MutableList<CountryDescriptionItemDto> = arrayListOf()
    private var listCountriesFromFilter: MutableList<CountryDescriptionItemDto> = arrayListOf()

    fun getCountriesFromApi(location: Location) {
        mCompositeDisposable.add(
            executeJob(
                mGetAllCountriesUseCase.execute()
                    .map { it.sortBySortStatusFromPref(sortStatus) }
                    .map {
                        it.forEach { country ->
                            if (location != defaultLocation) {
                                country.distance = calculateDistanceFiler(
                                    location,
                                    country
                                ).toString() + DEFAULT_KM
                            } else {
                                country.distance = N_A
                            }

                        }
                        return@map it
                    }, allCountriesLiveData
            )
        )
    }


    fun getCountriesFromDB() {
        mGetListCountriesFromDbUseCase.execute()
            .map { list ->
                list.convertDBdataToRetrofitModel(
                    mDatabaseLanguageRepository,
                    listOfCountriesFromDB
                )
            }
            ?.map { it -> it.sortBySortStatusFromPref(sortStatus) }
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

    fun saveToDBfromApi(listCountriesFromApiDto: MutableList<CountryDescriptionItemDto>) {
        Flowable.just(listCountriesFromApiDto)
            .doOnNext {
                val listOfAllCountries: MutableList<RoomCountryDescriptionItemDto> = mutableListOf()
                val listOfAllLanguages: MutableList<RoomLanguageOfOneCountryDto> = mutableListOf()
                it.forEach { item ->
                    listOfAllCountries.add(
                        RoomCountryDescriptionItemDto(
                            item.name,
                            item.capital,
                            item.area
                        )
                    )
                    item.languages.forEach { language ->
                        listOfAllLanguages.add(
                            RoomLanguageOfOneCountryDto(
                                item.name,
                                language.name
                            )
                        )
                    }
                }
                mDatabaseCountryRepository.addAll(listOfAllCountries)
                mDatabaseLanguageRepository.addAll(listOfAllLanguages)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                Log.d(KOIN_TAG, "Saved to DB")
            }, {
                Log.d(KOIN_TAG, it.message.toString())
            })
    }

    fun getCountriesFromSearch(): BehaviorSubject<String> {
        mCompositeDisposable.add(
            executeJob(
                mSearchSubject.toFlowable(BackpressureStrategy.LATEST)
                    .onErrorResumeNext { Flowable.just("") }
                    .filter { it.length >= MIN_SEARCH_STRING_LENGTH }
                    .debounce(DEBOUNCE_TIME_MILLIS, TimeUnit.MILLISECONDS)
                    .distinctUntilChanged()
                    .map { it.trim() }
                    .flatMap { text: String ->
                        mGetCountryListByNameUseCase.setParams(text).execute()
                            .map {
                                it.filter { country ->
                                    country.name.contains(text, true)
                                }
                                    .toMutableList()
                            }
                    }, countriesFromSearchAndFilterLiveData
            )
        )
        return mSearchSubject
    }

    fun getCountriesFromFilter(mapSettingsByFilter: HashMap<String?, Int>) {
        mGetAllCountriesUseCase.execute()
            .doOnNext { list ->
                val currentLocationOfUser = getResultOfCurrentLocation()
                listCountriesFromFilter.clear()
                list.forEach { country ->
                    makeListCountriesForFilter(country, mapSettingsByFilter, currentLocationOfUser)
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

    private fun makeListCountriesForFilter(
        country: CountryDescriptionItemDto,
        mapSettingsByFilter: HashMap<String?, Int>,
        currentLocationOfUser: Location
    ) {
        if (country.area >= mapSettingsByFilter[START_AREA_FILTER_KEY] ?: 0
            && country.area <= mapSettingsByFilter[END_AREA_FILTER_KEY] ?: 0
        ) {
            val distance = calculateDistanceFiler(currentLocationOfUser, country)
            country.distance = distance.toString() + DEFAULT_KM
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

    fun setSortStatus(value: Int) {
        sortStatus = value
    }
}