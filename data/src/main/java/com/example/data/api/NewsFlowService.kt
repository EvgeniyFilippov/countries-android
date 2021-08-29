package com.example.data.api

import com.example.data.NetConstants.SERVER_API_NEWS
import com.example.data.model.newsByCountry.NewsByCountry
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsFlowService {
    @Headers("X-Api-Key: 6a5cad1e580345b8b218480b1034d389")
    @GET (SERVER_API_NEWS)
    fun getListOfNews(@Query("country") alpha_2_ISO_3166_1: String): Flow<NewsByCountry>
}