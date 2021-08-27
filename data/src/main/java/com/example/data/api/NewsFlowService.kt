package com.example.data.api

import com.example.data.NetConstants.SERVER_API_NEWS
import com.example.data.model.newsByCountry.Article
import com.example.data.model.newsByCountry.NewsByCountry
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface NewsFlowService {
    @GET (SERVER_API_NEWS)
    fun getListOfNews(): Flow<List<NewsByCountry>>
}