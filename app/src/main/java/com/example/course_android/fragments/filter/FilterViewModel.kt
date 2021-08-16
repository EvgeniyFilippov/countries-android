package com.example.course_android.fragments.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.course_android.Constants.END_AREA_FILTER_KEY
import com.example.course_android.Constants.END_DISTANCE_FILTER_KEY
import com.example.course_android.Constants.END_POPULATION_FILTER_KEY
import com.example.course_android.Constants.FILTER_VALUE_FROM_KEY_AREA
import com.example.course_android.Constants.FILTER_VALUE_FROM_KEY_POPULATION
import com.example.course_android.Constants.FILTER_VALUE_TO_KEY_AREA
import com.example.course_android.Constants.FILTER_VALUE_TO_KEY_POPULATION
import com.example.course_android.Constants.START_AREA_FILTER_KEY
import com.example.course_android.Constants.START_DISTANCE_FILTER_KEY
import com.example.course_android.Constants.START_POPULATION_FILTER_KEY
import com.example.course_android.base.mvvm.*
import com.example.domain.repository.NetworkRepository
import com.example.domain.usecase.impl.GetAllCountriesUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class FilterViewModel(
    savedStateHandle: SavedStateHandle,
    private val mGetAllCountriesUseCase: GetAllCountriesUseCase,
) : BaseViewModel(savedStateHandle) {

    val mutableFilterLiveData = MutableLiveData<HashMap<String, Int>>()
    val mutableFilterConfigLiveData = savedStateHandle.getLiveData<Outcome<HashMap<String, Float>>>("configFilter")

    private val mapValuesByFilter = hashMapOf<String, Int>()
    private val mapConfigFilter = hashMapOf<String, Float>()

    fun putValuesFromFilter(
        startArea: Float,
        endArea: Float,
        startDistance: Int,
        endDistance: Int,
        startPopulation: Float,
        endPopulation: Float
    ) {
        mapValuesByFilter[START_AREA_FILTER_KEY] = startArea.toInt()
        mapValuesByFilter[END_AREA_FILTER_KEY] = endArea.toInt()
        mapValuesByFilter[START_DISTANCE_FILTER_KEY] = startDistance
        mapValuesByFilter[END_DISTANCE_FILTER_KEY] = endDistance
        mapValuesByFilter[START_POPULATION_FILTER_KEY] = startPopulation.toInt()
        mapValuesByFilter[END_POPULATION_FILTER_KEY] = endPopulation.toInt()

        mutableFilterLiveData.value = mapValuesByFilter
    }

    fun makeConfigFilter() {
        mGetAllCountriesUseCase.execute()
            .map { list -> listOf(
                list.minByOrNull { it.area.toInt()}?.area ?: 0.0,
                list.maxByOrNull { it.area.toInt()}?.area ?: 0.0,
                list.minByOrNull { it.population}?.population?.toDouble() ?: 0.0,
                list.maxByOrNull { it.population}?.population?.toDouble() ?: 0.0
            )}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mapConfigFilter[FILTER_VALUE_FROM_KEY_AREA] = it[0].toFloat()
                mapConfigFilter[FILTER_VALUE_TO_KEY_AREA] = it[1].toFloat()
                mapConfigFilter[FILTER_VALUE_FROM_KEY_POPULATION] = it[2].toFloat()
                mapConfigFilter[FILTER_VALUE_TO_KEY_POPULATION] = it[3].toFloat()
                mutableFilterConfigLiveData.next(mapConfigFilter)
            }, {
                mutableFilterConfigLiveData.failed(it)
            }, {
                if (mutableFilterConfigLiveData.value is Outcome.Next) {
                    mutableFilterConfigLiveData.success((mutableFilterConfigLiveData.value as Outcome.Next).data)
                }
            }).also { mCompositeDisposable.add(it) }

    }

}