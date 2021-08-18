package com.example.data.repository.network

import com.example.data.api.CoroutineCountryService
import com.example.data.ext.transformCapitalToDto
import com.example.domain.dto.model.CapitalItemDto
import com.example.domain.repository.NetworkCapitalsRepository

class NetworkCapitalRepositoryImpl(private val mService: CoroutineCountryService) :
    NetworkCapitalsRepository {

    override suspend fun getListOfCapitals(): MutableList<CapitalItemDto> {
        return mService.getListOfCapitals().transformCapitalToDto()
    }

}

