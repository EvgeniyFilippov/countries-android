package com.example.domain.usecase.impl

import com.example.domain.dto.news.NewsItemDto
import com.example.domain.repository.NetworkNewsFlowRepository
import com.example.domain.usecase.UseCaseFlow
import kotlinx.coroutines.flow.Flow

class GetNewsByNameFlowUseCase(private val mNetworkNewsFlowRepository: NetworkNewsFlowRepository) :
    UseCaseFlow<String, List<NewsItemDto>>() {

    override fun buildFlow(params: String?): Flow<List<NewsItemDto>> =
        mNetworkNewsFlowRepository.getListOfNews(params ?: "")

    override val mIsParamsRequired: Boolean
        get() = true

}