package com.example.course_android.api

import com.example.course_android.Constants.API_PATH_VALUE
import com.example.course_android.Constants.SERVER_API
import com.example.course_android.Constants.SERVER_API_DESCRIPTION
import com.example.course_android.model.oneCountry.CountryDescriptionItem
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