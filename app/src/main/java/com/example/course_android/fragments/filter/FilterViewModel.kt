package com.example.course_android.fragments.filter

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.course_android.Constants
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
import java.util.concurrent.TimeUnit

class FilterViewModel(
    private val start: Float,
    private val end: Float
) : BaseViewModel() {

    private val mutableCountriesLiveData = MutableLiveData<MutableList<CountryDescriptionItemDto>>()
    val countriesLiveData: LiveData<MutableList<CountryDescriptionItemDto>> =
        mutableCountriesLiveData

    private val mutableCountriesErrorLiveData = MutableLiveData<String>()
    val countriesErrorLiveData: LiveData<String> = mutableCountriesErrorLiveData

    private var listOfCountriesFromDB: MutableList<CountryDescriptionItemDto> = arrayListOf()
    private val countryDetailsDtoTransformer = CountryDetailsDtoTransformer()

    private var searchText: String = Constants.DEFAULT_STRING

    private var listCountriesFromFilter: MutableList<CountryDescriptionItemDto> = arrayListOf()

    fun getCountriesFromFilter() {
        RetrofitObj.getCountriesApi().getListOfCountry()
            .map { list -> countryDetailsDtoTransformer.transform(list) }
            .doOnNext { list -> list.forEach {
                if (it.area in start..end) {
                    listCountriesFromFilter.add(it)
                }
            } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mutableCountriesLiveData.value = listCountriesFromFilter
                listCountriesFromFilter.clear()
            }, {
            }).also { mCompositeDisposable.add(it) }
    }

}