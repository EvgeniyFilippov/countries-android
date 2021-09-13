package com.example.course_android.fragments.newsByLocation

import com.example.course_android.base.mvi.ViewState
import com.example.domain.dto.news.NewsItemDto

sealed class NewsState : ViewState {
    data class Loading(val loading : Boolean) : NewsState()
    data class ResultAllNews(val data : List<NewsItemDto>): NewsState()
    data class Exception(val callErrors: Throwable) : NewsState()
}