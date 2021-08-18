package com.example.domain.usecase.impl

import com.example.domain.dto.model.CapitalItemDto
import com.example.domain.dto.model.CountryDescriptionItemDto
import com.example.domain.repository.NetworkCapitalsRepository
import com.example.domain.repository.NetworkRepository
import com.example.domain.usecase.UseCase
import io.reactivex.rxjava3.core.Flowable

class GetCapitalsUseCase(private val networkCapitalsRepository: NetworkCapitalsRepository) : UseCase<Unit, MutableList<CapitalItemDto>>() {
    override fun buildFlowable(params: Unit?): Flowable<MutableList<CapitalItemDto>> = networkCapitalsRepository.getListOfCapitals()

    override val mIsParamsRequired: Boolean
        get() = false

}