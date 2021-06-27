package com.example.course_android

import com.example.course_android.model.CountriesDataItem
import retrofit2.Call
import retrofit2.http.GET

//https://restcountries.eu/rest/v2/all?fields=name;capital
interface CountriesApi {
    @GET("rest/v2/all")
    fun getTopHeadlines(): Call<List<CountriesDataItem>>
}