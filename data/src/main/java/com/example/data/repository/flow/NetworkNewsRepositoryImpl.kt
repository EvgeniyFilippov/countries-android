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

    override fun getListOfNewsOutcome(alpha_2_ISO_3166_1: String): Flow<Outcome<List<NewsItemDto>>> =
        modifyFlowOutcome(api.getListOfNews(alpha_2_ISO_3166_1).map { it.articles }, newsListTransformer)

    override fun getListOfNews(alpha_2_ISO_3166_1: String): Flow<List<NewsItemDto>> =
        modifyFlow(api.getListOfNews(alpha_2_ISO_3166_1).map { it.articles }, newsListTransformer)

}