package com.example.course_android.api

import com.example.course_android.model.allCountries.CountriesDataItem
import retrofit2.Call
import retrofit2.http.GET

interface CountriesApi {
    @GET("rest/v2/all")
    fun getTopHeadlines(): Call<List<CountriesDataItem>>
}