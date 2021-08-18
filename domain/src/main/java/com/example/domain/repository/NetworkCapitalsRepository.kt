package com.example.domain.repository

import com.example.domain.dto.model.CapitalItemDto
import com.example.domain.dto.model.CountryDescriptionItemDto
import io.reactivex.rxjava3.core.Flowable

interface NetworkCapitalsRepository {

    suspend fun getListOfCapitals(): MutableList<CapitalItemDto>

}