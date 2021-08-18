package com.example.data.repository.network

import com.example.data.api.CountryService
import com.example.data.ext.transformCountryToDto
import com.example.domain.dto.model.CountryDescriptionItemDto
import com.example.domain.repository.NetworkRepository
import io.reactivex.rxjava3.core.Flowable

class NetworkRepositoryImpl(private val mService: CountryService) :
    NetworkRepository {

    override fun getListOfCountry(): Flowable<MutableList<CountryDescriptionItemDto>> =
        mService.getListOfCountry().map { it.transformCountryToDto() }

    override fun getCountryDetails(country: String): Flowable<MutableList<CountryDescriptionItemDto>> =
        mService.getCountryDetails(country).map { it.transformCountryToDto() }
}