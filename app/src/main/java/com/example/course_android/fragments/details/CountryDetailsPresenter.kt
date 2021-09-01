package com.example.course_android.fragments.details

import com.example.course_android.base.mvp.BaseMvpPresenter
import com.example.domain.dto.news.NewsItemDto
import com.example.domain.repository.NetworkNewsFlowRepository
import com.example.domain.repository.NetworkNewsFlowableRepository
import com.example.domain.repository.NetworkRepository
import com.example.domain.usecase.impl.GetCountryListByNameUseCase

class CountryDetailsPresenter(
    private val mGetCountryListByNameUseCase: GetCountryListByNameUseCase,
    private val mNetworkNewsFlowableRepository: NetworkNewsFlowableRepository
) : BaseMvpPresenter<CountryDetailsView>() {

    fun getCountryInfo(mCountryName: String, isRefresh: Boolean) {
        addDisposable(
            inBackground(
                handleProgress(mGetCountryListByNameUseCase.setParams(mCountryName).execute(), isRefresh)
            ).subscribe({ response ->
                getView()?.showCountryInfo(response)
            }, {
                it.message?.let { it1 -> getView()?.showError(it1, it) }
            })
        )
    }

    fun getNews(alpha_2_ISO_3166_1: String, isRefresh: Boolean) {
        addDisposable(
            inBackground(
                handleProgress(mNetworkNewsFlowableRepository.getListOfNews(alpha_2_ISO_3166_1), isRefresh)
            ).subscribe({ response ->
                getView()?.showNews(response as MutableList<NewsItemDto>)
            }, {
                it.message?.let { it1 -> getView()?.showError(it1, it) }
            })
        )
    }



}