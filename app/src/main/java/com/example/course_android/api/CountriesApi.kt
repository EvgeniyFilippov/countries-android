package com.example.course_android.api

import com.example.course_android.Constants.SERVER_API
import com.example.course_android.model.oneCountry.CountryDescriptionItem
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

interface CountriesApi {

    @GET(SERVER_API)
    fun getListOfCountry(): Flowable<MutableList<CountryDescriptionItem>>

    @GET("rest/v2/name/{country}")
    fun getCountryDetails(
        @Path("country") country: String
    ): Flowable<MutableList<CountryDescriptionItem>>
}