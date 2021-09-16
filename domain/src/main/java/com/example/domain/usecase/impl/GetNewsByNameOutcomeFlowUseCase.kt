package com.example.domain.usecase.impl

import com.example.domain.dto.news.NewsItemDto
import com.example.domain.outcome.Outcome
import com.example.domain.repository.NetworkNewsFlowRepository
import com.example.domain.usecase.UseCaseOutcomeFlow
import kotlinx.coroutines.flow.Flow

class GetNewsByNameOutcomeFlowUseCase(private val mNetworkNewsFlowRepository: NetworkNewsFlowRepository) :
    UseCaseOutcomeFlow<String, List<NewsItemDto>>() {

    override val mIsParamsRequired: Boolean
        get() = true

    override fun buildOutcomeFlow(params: String?): Flow<Outcome<List<NewsItemDto>>> =
        mNetworkNewsFlowRepository.getListOfNewsOutcome(params ?: "")

}