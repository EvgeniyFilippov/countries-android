package com.example.data.repository.flow

import com.example.data.api.NewsFlowService
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

    override fun getListOfNews(): Flow<Outcome<List<NewsItemDto>>> =
        modifyFlow(api.getListOfNews().map { it.articles }, newsListTransformer)

}