package com.example.course_android.api

import com.example.course_android.model.oneCountry.CountryDescriptionItem
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryDescriptionApi {
    @GET("rest/v2/name/{country}")
    fun getTopHeadlines(
        @Path("country") country: String
    ): Flowable<MutableList<CountryDescriptionItem>>
}