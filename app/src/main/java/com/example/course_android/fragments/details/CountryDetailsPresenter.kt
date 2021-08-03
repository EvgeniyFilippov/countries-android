package com.example.course_android.fragments.details

import com.example.course_android.api.RetrofitObj
import com.example.course_android.base.mvp.BaseMvpPresenter
import com.example.course_android.dto.CountryDetailsDtoTransformer

class CountryDetailsPresenter : BaseMvpPresenter<CountryDetailsView>() {

    private val countryDetailsDtoTransformer = CountryDetailsDtoTransformer()

    fun getMyData(mCountryName: String, isRefresh: Boolean) {
        addDisposable(
            inBackground(
                handleProgress(RetrofitObj.getCountriesApi().getCountryDetails(mCountryName), isRefresh)
            ).subscribe({ response ->
                getView()?.showCountryInfo(
                    countryDetailsDtoTransformer.transform(response))
            }, {
                it.message?.let { it1 -> getView()?.showError(it1, it) }
            })
        )
    }
}