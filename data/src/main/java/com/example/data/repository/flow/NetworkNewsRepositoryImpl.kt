package com.example.data.repository.flow

import com.example.data.api.NewsFlowService
import com.example.data.ext.modifyFlowOutcome
import com.example.data.ext.modifyFlow
import com.example.data.model.newsByCountry.Article
import com.example.domain.dto.news.NewsItemDto
import com.example.domain.outcome.Outcome
import com.example.domain.outcome.Transformer
import com.example.domain.repository.NetworkNewsFlowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NetworkNewsRepositoryImpl(
    private val api: NewsFlowService,
    private val newsListTransformer: Transformer<List<Article>, List<NewsItemDto>>

) : NetworkNewsFlowRepository{

    override fun getListOfNewsOutcome(): Flow<Outcome<List<NewsItemDto>>> =
        modifyFlowOutcome(api.getListOfNews("ru", "6a5cad1e580345b8b218480b1034d389").map { it.articles }, newsListTransformer)

    override fun getListOfNews(): Flow<List<NewsItemDto>> =
        modifyFlow(api.getListOfNews("ru", "6a5cad1e580345b8b218480b1034d389").map { it.articles }, newsListTransformer)

}