package com.example.domain.repository

import com.example.domain.dto.room.RoomLanguageOfOneCountryDto
import io.reactivex.rxjava3.core.Flowable

interface DatabaseLanguageRepository {

//    fun add(entity: RoomLanguageOfOneCountryDto)

    fun getAllInfo(): Flowable<List<RoomLanguageOfOneCountryDto>>

    fun getLanguageByCountry(name: String): List<String>

    fun addAll(list: List<RoomLanguageOfOneCountryDto>)
}