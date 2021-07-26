package com.example.course_android.fragments.map

import com.example.course_android.CountriesApp
import com.example.course_android.api.CountriesApi
import com.example.course_android.api.RetrofitObj
import com.example.course_android.base.mvp.BaseMvpPresenter

class MapAllCountriesPresenter : BaseMvpPresenter<MapAllCountriesView>() {

    fun getAllCountries(isRefresh: Boolean) {
        RetrofitObj.getOkHttp()
        val countriesApi = CountriesApp.retrofit.create(CountriesApi::class.java)
        addDisposable(
            inBackground(
                handleProgress(countriesApi.getTopHeadlines(), isRefresh)
            ).subscribe({ response ->
                getView()?.showAllCountriesOnMap(response)
            }, {
                it.message?.let { it1 -> getView()?.showError(it1, it) }
            })
        )
    }

}