package com.example.domain.repository

import com.example.domain.dto.news.NewsItemDto
import com.example.domain.outcome.Outcome
import kotlinx.coroutines.flow.Flow

interface NetworkNewsFlowRepository {

    fun getListOfNews(): Flow<Outcome<List<NewsItemDto>>>

}