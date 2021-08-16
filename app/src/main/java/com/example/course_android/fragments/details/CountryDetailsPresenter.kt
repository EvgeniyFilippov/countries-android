package com.example.course_android.fragments.details

import com.example.course_android.base.mvp.BaseMvpPresenter
import com.example.domain.repository.NetworkRepository

class CountryDetailsPresenter(
    private val mNetworkRepository: com.example.domain.repository.NetworkRepository
) : BaseMvpPresenter<CountryDetailsView>() {

    fun getMyData(mCountryName: String, isRefresh: Boolean) {
        addDisposable(
            inBackground(
                handleProgress(mNetworkRepository.getCountryDetails(mCountryName), isRefresh)
            ).subscribe({ response ->
                getView()?.showCountryInfo( response)
            }, {
                it.message?.let { it1 -> getView()?.showError(it1, it) }
            })
        )
    }
}