package com.example.course_android.fragments.news

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.course_android.Constants
import com.example.course_android.Constants.RU
import com.example.course_android.base.mvvm.BaseViewModel
import com.example.domain.dto.news.NewsItemDto
import com.example.domain.outcome.Outcome
import com.example.domain.usecase.impl.GetNewsByNameFlowUseCase
import com.example.domain.usecase.impl.GetNewsByNameOutcomeFlowUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NewsViewModel(
    savedStateHandle: SavedStateHandle,
    private val mGetNewsByNameOutcomeFlowUseCase: GetNewsByNameOutcomeFlowUseCase,
    private val mGetNewsByNameFlowUseCase: GetNewsByNameFlowUseCase
) : BaseViewModel(savedStateHandle) {

    fun getNewsFlow(): Flow<Outcome<List<NewsItemDto>>> =
        mGetNewsByNameOutcomeFlowUseCase.setParams(RU).execute()

    var searchText = MutableStateFlow("")

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun getNewsFromSearch(): Flow<Outcome<List<NewsItemDto>>> =
        searchText
            .filter { it.length >= Constants.MIN_SEARCH_STRING_LENGTH }
            .debounce(500)
            .distinctUntilChanged()
            .flatMapLatest { text ->
                mGetNewsByNameFlowUseCase.setParams(RU).execute()
                    .map {
                        it.filter { news ->
                            news.title.contains(text, true)
                        }
                    }
            }
            .flowOn(Dispatchers.IO)
            .map { list -> Outcome.success(list) }

}