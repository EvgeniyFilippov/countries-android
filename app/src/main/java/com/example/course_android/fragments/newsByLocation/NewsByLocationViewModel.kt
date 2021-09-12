package com.example.course_android.fragments.newsByLocation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.course_android.Constants
import com.example.course_android.base.mvi.BaseMviViewModel
import com.example.domain.dto.news.NewsItemDto
import com.example.domain.outcome.Outcome
import com.example.domain.usecase.impl.GetNewsByNameFlowUseCase
import com.example.domain.usecase.impl.GetNewsByNameOutcomeFlowUseCase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NewsByLocationViewModel (private val mGetNewsByNameOutcomeFlowUseCase: GetNewsByNameOutcomeFlowUseCase) :
    BaseMviViewModel<NewsIntent, NewsAction, NewsState>() {
    override fun intentToAction(intent: NewsIntent): NewsAction {
        return when (intent) {
            is NewsIntent.LoadAllCharacters -> NewsAction.AllCharacters
        }
    }

    private fun getNewsFlow(): Flow<Outcome<List<NewsItemDto>>> =
        mGetNewsByNameOutcomeFlowUseCase.setParams(Constants.RU).execute()

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