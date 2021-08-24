package com.example.domain.usecase.impl

import com.example.domain.dto.model.CountryDescriptionItemDto
import com.example.domain.repository.NetworkRepository
import com.example.domain.usecase.UseCase
import io.reactivex.rxjava3.core.Flowable

class GetCountryListByNameUseCase(private val mNetworkRepository: NetworkRepository) : UseCase<String, MutableList<CountryDescriptionItemDto>>() {

    override fun buildFlowable(params: String?): Flowable<MutableList<CountryDescriptionItemDto>> = mNetworkRepository.getCountryDetails(params ?: "")

    override val mIsParamsRequired: Boolean
        get() = true
}