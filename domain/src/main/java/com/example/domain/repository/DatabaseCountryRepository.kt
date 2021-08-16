package com.example.domain.repository

import com.example.domain.dto.room.RoomCountryDescriptionItemDto
import io.reactivex.rxjava3.core.Flowable

interface DatabaseCountryRepository {

    fun getAllInfo(): Flowable<List<RoomCountryDescriptionItemDto>>

    fun add(entity: RoomCountryDescriptionItemDto)

    fun addAll(list: List<RoomCountryDescriptionItemDto>)
}