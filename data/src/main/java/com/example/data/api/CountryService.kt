package com.example.data.api

import com.example.data.NetConstants.API_PATH_VALUE
import com.example.data.NetConstants.SERVER_API
import com.example.data.NetConstants.SERVER_API_CAPITALS
import com.example.data.NetConstants.SERVER_API_DESCRIPTION
import com.example.data.model.capitals.CapitalItem
import com.example.data.model.oneCountry.CountryDescriptionItem
import com.example.domain.dto.model.CapitalItemDto
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryService {

    @GET(SERVER_API)
    fun getListOfCountry(): Flowable<MutableList<CountryDescriptionItem>>

    @GET(SERVER_API_DESCRIPTION)
    fun getCountryDetails(
        @Path(API_PATH_VALUE) country: String
    ): Flowable<MutableList<CountryDescriptionItem>>

}