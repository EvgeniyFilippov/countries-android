package com.example.domain.usecase.impl

import com.example.domain.dto.model.CapitalItemDto
import com.example.domain.repository.NetworkCapitalsRepository
import com.example.domain.usecase.UseCaseCoroutines

class GetCapitalsUseCase(private val networkCapitalsRepository: NetworkCapitalsRepository) :
    UseCaseCoroutines<Unit, MutableList<CapitalItemDto>>() {

    override suspend fun buildResult(params: Unit?): MutableList<CapitalItemDto> =
        networkCapitalsRepository.getListOfCapitals()

    override val mIsParamsRequired: Boolean
        get() = false

}