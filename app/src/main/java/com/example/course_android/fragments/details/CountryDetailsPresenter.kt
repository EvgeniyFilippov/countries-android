package com.example.course_android.fragments.details

import com.example.course_android.base.mvp.BaseMvpPresenter
import com.example.domain.repository.NetworkRepository
import com.example.domain.usecase.impl.GetCountryListByNameUseCase

class CountryDetailsPresenter(
    private val mGetCountryListByNameUseCase: GetCountryListByNameUseCase
) : BaseMvpPresenter<CountryDetailsView>() {

    fun getMyData(mCountryName: String, isRefresh: Boolean) {
        addDisposable(
            inBackground(
                handleProgress(mGetCountryListByNameUseCase.setParams(mCountryName).execute(), isRefresh)
            ).subscribe({ response ->
                getView()?.showCountryInfo( response)
            }, {
                it.message?.let { it1 -> getView()?.showError(it1, it) }
            })
        )
    }
}