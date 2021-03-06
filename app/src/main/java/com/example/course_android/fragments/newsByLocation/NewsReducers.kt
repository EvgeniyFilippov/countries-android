package com.example.course_android.fragments.newsByLocation

import com.example.domain.dto.news.NewsItemDto
import com.example.domain.outcome.Outcome

fun Outcome<List<NewsItemDto>>.reduce(): NewsState {
    return when (this) {
        is Outcome.Success -> NewsState.ResultAllNews(data)
        is Outcome.Failure -> NewsState.Exception(e)
        is Outcome.Progress -> NewsState.Loading(loading)
        else -> NewsState.Exception(Throwable(""))
    }
}