package com.example.course_android.fragments.newsByLocation

import android.content.Context
import android.location.Geocoder
import com.example.course_android.Constants.DEFAULT_COUNTRY_CODE
import com.example.course_android.Constants.RU
import com.example.course_android.base.mvi.BaseMviViewModel
import com.example.course_android.utils.getMyLocation
import com.example.domain.dto.news.NewsItemDto
import com.example.domain.outcome.Outcome
import com.example.domain.usecase.impl.GetNewsByNameOutcomeFlowUseCase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class NewsByLocationViewModel (private val mGetNewsByNameOutcomeFlowUseCase: GetNewsByNameOutcomeFlowUseCase, context: Context ) :
    BaseMviViewModel<NewsIntent, NewsAction, NewsState>() {
    override fun intentToAction(intent: NewsIntent): NewsAction {
        return when (intent) {
            is NewsIntent.LoadAllCharacters -> NewsAction.AllCharacters
        }
    }

    private fun getCountryCode(context: Context) : String {
        var countryCode = DEFAULT_COUNTRY_CODE
        val geocoder = Geocoder(context)
        val myLocation = getMyLocation(context)
        val answerFromGeocoder = geocoder.getFromLocation(myLocation.latitude, myLocation.longitude, 1)
        if (answerFromGeocoder.size == 1) {
            countryCode = answerFromGeocoder[0].countryCode
        }
         return countryCode
    }

    private val countryCode = getCountryCode(context)


    private fun getNewsFlow(): Flow<Outcome<List<NewsItemDto>>> =
        mGetNewsByNameOutcomeFlowUseCase.setParams(countryCode).execute()

    @InternalCoroutinesApi
    override fun handleAction(action: NewsAction) {
        launchOnUI {
            when (action) {
                is NewsAction.AllCharacters -> {
                    getNewsFlow().collect {
                        mState.postValue(it.reduce())
                    }
                }
            }
        }
    }

}