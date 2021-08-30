package com.example.course_android.fragments.news

import androidx.lifecycle.SavedStateHandle
import com.example.course_android.Constants
import com.example.course_android.Constants.RU
import com.example.course_android.base.mvvm.BaseViewModel
import com.example.domain.dto.news.NewsItemDto
import com.example.domain.outcome.Outcome
import com.example.domain.repository.NetworkNewsFlowRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

class NewsViewModel(
    savedStateHandle: SavedStateHandle,
    private val mNetworkNewsFlowRepository: NetworkNewsFlowRepository,
) : BaseViewModel(savedStateHandle) {

    fun getNewsFlow(): Flow<Outcome<List<NewsItemDto>>> =
        mNetworkNewsFlowRepository.getListOfNewsOutcome(RU)

    var searchText = MutableStateFlow("")

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun getNewsFromSearch(): Flow<Outcome<List<NewsItemDto>>> =
          searchText
                .filter { it.length >= Constants.MIN_SEARCH_STRING_LENGTH }
                .debounce(500)
                .distinctUntilChanged()
                .flatMapLatest { text ->
                    mNetworkNewsFlowRepository.getListOfNews(RU)
                        .map {
                            it.filter { news ->
                                news.title.contains(text, true)
                            }
                        }
                }
                .flowOn(Dispatchers.IO)
                .map { list -> Outcome.success(list) }
                .onStart { emit(Outcome.loading(true)) }
                .onCompletion { emit(Outcome.loading(false)) }
                .catch { ex -> emit(Outcome.failure(ex)) }

    fun doOnListItemClick() {

    }

}