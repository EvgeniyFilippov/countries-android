package com.example.course_android.fragments.news

import androidx.lifecycle.SavedStateHandle
import com.example.course_android.base.mvvm.BaseViewModel
import com.example.domain.dto.news.NewsItemDto
import com.example.domain.outcome.Outcome
import com.example.domain.repository.NetworkNewsFlowRepository
import kotlinx.coroutines.flow.Flow

class NewsViewModel (
    savedStateHandle: SavedStateHandle,
    private val mNetworkNewsFlowRepository: NetworkNewsFlowRepository
) : BaseViewModel(savedStateHandle) {

    fun getNewsFlow(): Flow<Outcome<List<NewsItemDto>>> =
        mNetworkNewsFlowRepository.getListOfNews()

}