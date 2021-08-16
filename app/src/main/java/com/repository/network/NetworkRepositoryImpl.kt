package com.repository.network

import com.example.course_android.api.CountryService
import com.example.course_android.dto.model.CountryDescriptionItemDto
import com.example.course_android.dto.transformCountryToDto
import io.reactivex.rxjava3.core.Flowable

class NetworkRepositoryImpl(private val mService: CountryService) :
    com.example.domain.repository.NetworkRepository {

    override fun getListOfCountry(): Flowable<MutableList<CountryDescriptionItemDto>> =
        mService.getListOfCountry().map { it.transformCountryToDto() }

    override fun getCountryDetails(country: String): Flowable<MutableList<CountryDescriptionItemDto>> =
        mService.getCountryDetails(country).map { it.transformCountryToDto() }
}