package com.example.data.api

import com.example.data.NetConstants.SERVER_API_CAPITALS
import com.example.data.model.capitals.CapitalItem
import retrofit2.http.GET

interface CoroutineCountryService {

    @GET(SERVER_API_CAPITALS)
    suspend fun getListOfCapitals(): MutableList<CapitalItem>
}