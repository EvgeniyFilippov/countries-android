package com.example.course_android.fragments.newsByLocation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.example.course_android.Constants.DEFAULT_COUNTRY_CODE
import com.example.course_android.Constants.RU
import com.example.course_android.base.mvi.BaseMviViewModel

import com.example.course_android.utils.getMyLocation
import com.example.domain.dto.news.NewsItemDto
import com.example.domain.outcome.Outcome
import com.example.domain.usecase.impl.GetNewsByNameOutcomeFlowUseCase
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class NewsByLocationViewModel(
    private val mGetNewsByNameOutcomeFlowUseCase: GetNewsByNameOutcomeFlowUseCase,
    context: Context
) :
    BaseMviViewModel<NewsIntent, NewsAction, NewsState>() {
    override fun intentToAction(intent: NewsIntent): NewsAction {
        return when (intent) {
            is NewsIntent.LoadAllCharacters -> NewsAction.AllCharacters
        }
    }

    private val context2 = context

    private fun getCountryCode(context: Context): String {
        var countryCode = DEFAULT_COUNTRY_CODE
        val geocoder = Geocoder(context)
        val myLocation = getMyLocation(context)
        val answerFromGeocoder =
            geocoder.getFromLocation(myLocation.latitude, myLocation.longitude, 1)
        if (answerFromGeocoder.size == 1) {
            countryCode = answerFromGeocoder[0].countryCode
        }
        return countryCode
    }


    @SuppressLint("MissingPermission")
    @InternalCoroutinesApi
    override fun handleAction(action: NewsAction) {
        launchOnUI {
            when (action) {
                is NewsAction.AllCharacters -> {
                    LocationServices.getFusedLocationProviderClient(context2)
                        .lastLocation
                        .addOnSuccessListener { location ->
                            var countryCode = DEFAULT_COUNTRY_CODE
                            val geocoder = Geocoder(context2)
                            val answerFromGeocoder =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            if (answerFromGeocoder.size == 1) {
                                countryCode = answerFromGeocoder[0].countryCode
                            }

                            launchOnUI {
                                mGetNewsByNameOutcomeFlowUseCase.setParams(countryCode)
                                    .execute().collect {
                                        mState.postValue(it.reduce())
                                    }
                            }

                        }
                }
            }
        }
    }

}