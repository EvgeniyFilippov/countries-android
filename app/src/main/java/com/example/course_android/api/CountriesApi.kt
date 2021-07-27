package com.example.course_android.api

import com.example.course_android.Constants.SERVER_API
import com.example.course_android.model.allCountries.CountriesDataItem
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET

interface CountriesApi {
    @GET(SERVER_API)
    fun getTopHeadlines(): Flowable<List<CountriesDataItem>>
}