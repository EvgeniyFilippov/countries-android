package com.example.course_android.fragments.map

import com.example.course_android.base.mvp.BaseMvpPresenter
import com.repository.network.NetworkRepository

class MapAllCountriesPresenter(
    private val mNetworkRepository: NetworkRepository
) : BaseMvpPresenter<MapAllCountriesView>() {

    fun getAllCountries(isRefresh: Boolean) {
        addDisposable(
            inBackground(
                handleProgress(mNetworkRepository.getListOfCountry(), isRefresh)
            ).subscribe({ response ->
                getView()?.showAllCountriesOnMap(response)
            }, {
                it.message?.let { it1 -> getView()?.showError(it1, it) }
            })
        )
    }

}