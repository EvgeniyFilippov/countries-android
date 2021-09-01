package com.example.data.repository.network

import com.example.data.api.NewsFlowService
import com.example.data.api.NewsFlowableService
import com.example.data.ext.modifyFlowOutcome
import com.example.data.ext.modifyFlow
import com.example.data.model.newsByCountry.Article
import com.example.domain.dto.news.NewsItemDto
import com.example.domain.outcome.Outcome
import com.example.domain.outcome.Transformer
import com.example.domain.repository.NetworkNewsFlowRepository
import com.example.domain.repository.NetworkNewsFlowableRepository
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NetworkNewsFlowableRepositoryImpl(
    private val api: NewsFlowableService,
    private val newsListTransformer: Transformer<List<Article>, List<NewsItemDto>>

) : NetworkNewsFlowableRepository {

    override fun getListOfNews(alpha_2_ISO_3166_1: String): Flowable<List<NewsItemDto>> =
        api.getListOfNews(alpha_2_ISO_3166_1)
            .map { it.articles }
            .map { newsListTransformer.convert(it) }

}