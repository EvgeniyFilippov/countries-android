package com.repository.network

import com.example.course_android.dto.model.CountryDescriptionItemDto
import io.reactivex.rxjava3.core.Flowable

interface NetworkRepository {

    fun getListOfCountry(): Flowable<MutableList<CountryDescriptionItemDto>>

    fun getCountryDetails(country: String): Flowable<MutableList<CountryDescriptionItemDto>>

}