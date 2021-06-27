package com.example.course_android

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//https://restcountries.eu/rest/v2/all?fields=name;capital
interface CountriesApi {
    @GET("v2/top-headlines")
    fun getTopHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): Call<NewsRootObject>
}