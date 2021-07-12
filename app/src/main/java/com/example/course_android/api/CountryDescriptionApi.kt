package com.example.course_android.api

import com.example.course_android.model.oneCountry.CountryDescriptionItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CountryDescriptionApi {
    @GET("rest/v2/name/{country}")
    fun getTopHeadlines(
        @Path("country") country: String
    ): Call<List<CountryDescriptionItem>>
}