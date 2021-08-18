package com.example.data.repository.network

import com.example.data.api.CountryService
import com.example.data.ext.transformCapitalToDto
import com.example.data.ext.transformCountryToDto
import com.example.domain.dto.model.CapitalItemDto
import com.example.domain.dto.model.CountryDescriptionItemDto
import com.example.domain.repository.NetworkCapitalsRepository
import com.example.domain.repository.NetworkRepository
import io.reactivex.rxjava3.core.Flowable

class NetworkCapitalRepositoryImpl(private val mService: CountryService) :
    NetworkCapitalsRepository {

    override fun getListOfCapitals(): Flowable<MutableList<CapitalItemDto>> =
        mService.getListOfCapitals().map { it.transformCapitalToDto() }.map {
            it.filter { capital ->
                !capital.capital.equals("", true)
            }
                .toMutableList()
        }

}