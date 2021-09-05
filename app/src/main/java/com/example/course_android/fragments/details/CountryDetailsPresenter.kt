package com.example.course_android.fragments.details

import com.example.course_android.base.mvp.BaseMvpPresenter
import com.example.domain.dto.news.NewsItemDto
import com.example.domain.repository.NetworkNewsFlowableRepository
import com.example.domain.usecase.impl.GetCountryListByNameUseCase

class CountryDetailsPresenter(
    private val mGetCountryListByNameUseCase: GetCountryListByNameUseCase,
    private val mNetworkNewsFlowableRepository: NetworkNewsFlowableRepository
) : BaseMvpPresenter<CountryDetailsView>() {

    fun getCountryInfo(mCountryName: String) {
        addDisposable(
            inBackground(
                mGetCountryListByNameUseCase.setParams(mCountryName).execute()
            ).subscribe({ response ->
                getView()?.showCountryInfo(response)
            }, {
                it.message?.let { it1 -> getView()?.showError(it1, it) }
            })
        )
    }

    fun getNews(alpha_2_ISO_3166_1: String) {
        addDisposable(
            inBackground(
                mNetworkNewsFlowableRepository.getListOfNews(alpha_2_ISO_3166_1)
            ).subscribe({ response ->
                getView()?.showNews(response as MutableList<NewsItemDto>)
            }, {
                it.message?.let { it1 -> getView()?.showError(it1, it) }
            })
        )
    }



}