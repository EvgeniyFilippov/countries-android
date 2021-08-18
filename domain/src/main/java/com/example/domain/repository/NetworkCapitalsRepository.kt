package com.example.domain.repository

import com.example.domain.dto.model.CapitalItemDto

interface NetworkCapitalsRepository {

    suspend fun getListOfCapitals(): MutableList<CapitalItemDto>

}