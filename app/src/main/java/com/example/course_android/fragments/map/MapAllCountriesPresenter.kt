package com.example.course_android.fragments.map

import com.example.course_android.api.RetrofitObj
import com.example.course_android.base.mvp.BaseMvpPresenter
import com.example.course_android.dto.CountryDetailsDtoTransformer

class MapAllCountriesPresenter : BaseMvpPresenter<MapAllCountriesView>() {

    private val countryDetailsDtoTransformer = CountryDetailsDtoTransformer()

    fun getAllCountries(isRefresh: Boolean) {
        addDisposable(
            inBackground(
                handleProgress(RetrofitObj.getCountriesApi().getListOfCountry(), isRefresh)
            ).subscribe({ response ->
                getView()?.showAllCountriesOnMap(
                    countryDetailsDtoTransformer.transform(response))
            }, {
                it.message?.let { it1 -> getView()?.showError(it1, it) }
            })
        )
    }

}