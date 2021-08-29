package com.example.course_android.fragments.news

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.course_android.Constants
import com.example.course_android.Constants.NEWS_FROM_SEARCH_LIVE_DATA
import com.example.course_android.base.mvvm.BaseViewModel
import com.example.domain.dto.news.NewsItemDto
import com.example.domain.outcome.Outcome
import com.example.domain.repository.NetworkNewsFlowRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NewsViewModel(
    savedStateHandle: SavedStateHandle,
    private val mNetworkNewsFlowRepository: NetworkNewsFlowRepository,
) : BaseViewModel(savedStateHandle) {

    fun getNewsFlow(): Flow<Outcome<List<NewsItemDto>>> =
        mNetworkNewsFlowRepository.getListOfNewsOutcome()

    val newsFromSearchLiveData =
        savedStateHandle.getLiveData<Outcome<MutableList<NewsItemDto>>>(
            NEWS_FROM_SEARCH_LIVE_DATA
        )

    private var searchText = MutableStateFlow("")

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun getNewsFromSearch(): MutableStateFlow<String> {
        viewModelScope.launch {
            searchText
                .filter { it.length >= Constants.MIN_SEARCH_STRING_LENGTH }
                .debounce(500)
                .distinctUntilChanged()
                .flatMapLatest { text ->
                    mNetworkNewsFlowRepository.getListOfNews()
                        .map {
                            it.filter { news ->
                                news.title.contains(text, true)
                            }
                                .toMutableList()
                        }
                }
                .map { it }
                .flowOn(Dispatchers.IO)
//                .map { list -> Outcome.success(list) }
//                .onStart { emit(Outcome.loading(true)) }
//                .onCompletion { emit(Outcome.loading(false)) }
//                .catch { ex -> emit(Outcome.failure(ex)) }
                .collect { result -> newsFromSearchLiveData.value = Outcome.success(result) }
//                .collect { result ->
//                    Log.d(KOIN_TAG, "POISK " + result)
//                }
        }
        return searchText
    }

}