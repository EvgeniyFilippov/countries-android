package com.example.course_android.fragments.filter

import androidx.lifecycle.MutableLiveData
import com.example.course_android.Constants.END_AREA_FILTER_KEY
import com.example.course_android.Constants.END_DISTANCE_FILTER_KEY
import com.example.course_android.Constants.FILTER_VALUE_FROM_KEY
import com.example.course_android.Constants.FILTER_VALUE_TO_KEY
import com.example.course_android.Constants.START_AREA_FILTER_KEY
import com.example.course_android.Constants.START_DISTANCE_FILTER_KEY
import com.example.course_android.api.RetrofitObj
import com.example.course_android.base.mvvm.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class FilterViewModel() : BaseViewModel() {

    val mutableFilterLiveData = MutableLiveData<HashMap<String, Int>>()
    val mutableFilterConfigLiveData = MutableLiveData<HashMap<String, Float>>()

    private val mapValuesByFilter = hashMapOf<String, Int>()
    private val mapConfigFilter = hashMapOf<String, Float>()

    fun putValuesFromFilter(
        startArea: Float,
        endArea: Float,
        startDistance: Int,
        endDistance: Int
    ) {
        mapValuesByFilter[START_AREA_FILTER_KEY] = startArea.toInt()
        mapValuesByFilter[END_AREA_FILTER_KEY] = endArea.toInt()
        mapValuesByFilter[START_DISTANCE_FILTER_KEY] = startDistance
        mapValuesByFilter[END_DISTANCE_FILTER_KEY] = endDistance
        mutableFilterLiveData.value = mapValuesByFilter
    }

    fun makeConfigFilter() {
        var minArea = 0.0
        var maxArea = 0.0
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
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mapConfigFilter[FILTER_VALUE_FROM_KEY] = minArea.toFloat()
                mapConfigFilter[FILTER_VALUE_TO_KEY] = maxArea.toFloat()
                mutableFilterConfigLiveData.value = mapConfigFilter
            }, {

            }).also { mCompositeDisposable.add(it) }
    }

}