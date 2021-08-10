package com.example.course_android.fragments.details

import com.example.course_android.api.RetrofitObj
import com.example.course_android.base.mvp.BaseMvpPresenter
import com.example.course_android.dto.transformCountryToDto
import com.repository.network.NetworkRepository

class CountryDetailsPresenter(
    private val mNetworkRepository: NetworkRepository
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