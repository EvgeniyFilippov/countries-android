package com.example.course_android.fragments.map

import com.example.course_android.base.mvp.BaseMvpPresenter
import com.example.domain.usecase.impl.GetAllCountriesUseCase

class MapAllCountriesPresenter(
    private val mGetAllCountriesUseCase: GetAllCountriesUseCase
) : BaseMvpPresenter<MapAllCountriesView>() {

    fun getAllCountries() {
        addDisposable(
            inBackground(
                mGetAllCountriesUseCase.execute()
            ).subscribe({ response ->
                getView()?.showAllCountriesOnMap(response)
            }, {
                it.message?.let { it1 -> getView()?.showError(it1, it) }
            })
        )
    }

}