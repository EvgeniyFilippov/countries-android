package com.example.domain.repository

import com.example.domain.dto.model.CountryDescriptionItemDto
import io.reactivex.rxjava3.core.Flowable

interface NetworkRepository {

    fun getListOfCountry(): Flowable<MutableList<CountryDescriptionItemDto>>

    fun getCountryDetails(country: String): Flowable<MutableList<CountryDescriptionItemDto>>

}