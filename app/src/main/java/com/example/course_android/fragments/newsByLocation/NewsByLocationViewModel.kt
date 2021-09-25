package com.example.course_android.fragments.newsByLocation

import android.annotation.SuppressLint
import android.content.Context
import com.example.course_android.Constants.NO_CODE
import com.example.course_android.base.mvi.BaseMviViewModel
import com.example.course_android.utils.getGeocoder
import com.example.course_android.utils.getLocationProviderClient
import com.example.domain.usecase.impl.GetNewsByNameOutcomeFlowUseCase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

class NewsByLocationViewModel(
    private val mGetNewsByNameOutcomeFlowUseCase: GetNewsByNameOutcomeFlowUseCase,
    context: Context
) :
    BaseMviViewModel<NewsIntent, NewsAction, NewsState>() {
    override fun intentToAction(intent: NewsIntent): NewsAction {
        return when (intent) {
            is NewsIntent.LoadNewsIntent -> NewsAction.LoadNewsAction
        }
    }

    private val locationProviderClient = getLocationProviderClient(context)
    private val geocoder = getGeocoder(context)

    @SuppressLint("MissingPermission")
    @InternalCoroutinesApi
    override fun handleAction(action: NewsAction) {
        launchOnUI {
            when (action) {
                is NewsAction.LoadNewsAction -> {
                    locationProviderClient
                        .lastLocation
                        .addOnSuccessListener { location ->
                            var countryCode = NO_CODE
                            if (location != null) {
                                val answerFromGeocoder =
                                    geocoder.getFromLocation(
                                        location.latitude,
                                        location.longitude,
                                        1
                                    )
                                if (answerFromGeocoder.size == 1) {
                                    countryCode = answerFromGeocoder[0].countryCode
                                }
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