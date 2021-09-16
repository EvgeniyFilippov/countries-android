package com.example.domain.repository

import com.example.domain.dto.news.NewsItemDto
import com.example.domain.outcome.Outcome
import kotlinx.coroutines.flow.Flow

interface NetworkNewsFlowRepository {

    fun getListOfNewsOutcome(alpha_2_ISO_3166_1: String): Flow<Outcome<List<NewsItemDto>>>

    fun getListOfNews(alpha_2_ISO_3166_1: String): Flow<List<NewsItemDto>>

}