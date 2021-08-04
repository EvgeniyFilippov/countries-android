package com.example.course_android.fragments.filter

import android.content.ContentValues
import android.os.Parcel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.course_android.Constants
import com.example.course_android.Constants.END_AREA_FILTER_KEY
import com.example.course_android.Constants.START_AREA_FILTER_KEY
import com.example.course_android.CountriesApp
import com.example.course_android.api.RetrofitObj
import com.example.course_android.base.mvvm.BaseViewModel
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
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.TypeParceler
import java.util.concurrent.TimeUnit

class FilterViewModel() : BaseViewModel() {

    val mutableCountriesLiveData = MutableLiveData<HashMap<String, Float>>()

    private val map = hashMapOf<String, Float>()

    fun getCountriesFromFilter(start: Float, end: Float) {

        map[START_AREA_FILTER_KEY] = start
        map[END_AREA_FILTER_KEY] = end
        mutableCountriesLiveData.value = map


    }


}