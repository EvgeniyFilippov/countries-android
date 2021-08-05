package com.example.course_android.fragments.filter

import androidx.lifecycle.LiveData
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
import com.example.course_android.api.RetrofitObj
import com.example.course_android.base.mvvm.*
import com.example.course_android.dto.model.CountryDescriptionItemDto
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class FilterViewModel(savedStateHandle: SavedStateHandle) : BaseViewModel(savedStateHandle) {

    val mutableFilterLiveData = MutableLiveData<HashMap<String, Int>>()
//    val mutableFilterConfigLiveData = MutableLiveData<HashMap<String, Float>>()
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
        var minArea = 0.0
        var maxArea = 0.0
        var minPopilation = 0
        var maxPopulation = 0
        RetrofitObj.getCountriesApi().getListOfCountry()
            .doOnNext {  list ->
                list.forEach { country ->
                    if (country.area != null) {
                        if (country.area <= minArea) {
                            minArea = country.area
                        }
                        if (country.area >= maxArea) {
                            maxArea = country.area
                        }
                    }
                    if (country.population != null) {
                        if (country.population <= minPopilation) {
                            minPopilation = country.population
                        }
                        if (country.population >= maxPopulation) {
                            maxPopulation = country.population
                        }
                    }
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mapConfigFilter[FILTER_VALUE_FROM_KEY_AREA] = minArea.toFloat()
                mapConfigFilter[FILTER_VALUE_TO_KEY_AREA] = maxArea.toFloat()
                mapConfigFilter[FILTER_VALUE_FROM_KEY_POPULATION] = minPopilation.toFloat()
                mapConfigFilter[FILTER_VALUE_TO_KEY_POPULATION] = maxPopulation.toFloat()
                mutableFilterConfigLiveData.next(mapConfigFilter)
            }, {
                mutableFilterConfigLiveData.failed(it)
            }, {
                if (mutableFilterConfigLiveData.value is Outcome.Next) {
                    mutableFilterConfigLiveData.success((mutableFilterConfigLiveData.value as Outcome.Next).data)
                }
            }).also { mCompositeDisposable.add(it) }



//            .subscribe({
//
////                savedStateHandle["giveMeYouMoney"] = mapConfigFilter
//                setConfigFilter(mapConfigFilter)
//
////                mutableFilterConfigLiveData.value = mapConfigFilter
//            }, {


    }

    fun setConfigFilter(config: HashMap<String, Float>) {
        savedStateHandle["configFilter"] = config
    }


}