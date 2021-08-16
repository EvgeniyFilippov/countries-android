package com.example.domain.usecase.impl

import com.example.domain.dto.model.CountryDescriptionItemDto
import com.example.domain.repository.NetworkRepository
import com.example.domain.usecase.UseCase
import io.reactivex.rxjava3.core.Flowable

class GetAllCountriesUseCase(private val networkRepository: NetworkRepository) : UseCase<Unit, MutableList<CountryDescriptionItemDto>>() {
    override fun buildFlowable(params: Unit?): Flowable<MutableList<CountryDescriptionItemDto>> = networkRepository.getListOfCountry()

    override val mIsParamsRequired: Boolean
        get() = false

}