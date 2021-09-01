package com.example.domain.repository

import com.example.domain.dto.news.NewsItemDto
import com.example.domain.outcome.Outcome
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.flow.Flow

interface NetworkNewsFlowableRepository {

    fun getListOfNews(alpha_2_ISO_3166_1: String): Flowable<List<NewsItemDto>>

}