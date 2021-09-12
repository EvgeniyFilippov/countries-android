package com.example.course_android.fragments.newsByLocation

import com.example.course_android.base.mvi.ViewState
import com.example.domain.dto.news.NewsItemDto

sealed class NewsState : ViewState {
    object Loading : NewsState()
    data class ResultAllPersona(val data : List<NewsItemDto>): NewsState()
    data class ResultSearch(val data : List<NewsItemDto>): NewsState()
//    data class Exception(val callErrors: CallErrors) : NewsState()
}