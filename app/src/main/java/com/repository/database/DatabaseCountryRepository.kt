package com.repository.database

import com.example.course_android.dto.room.RoomCountryDescriptionItemDto
import com.example.course_android.room.CountryBaseInfoEntity
import io.reactivex.rxjava3.core.Flowable

interface DatabaseCountryRepository {

    fun getAllInfo(): Flowable<List<RoomCountryDescriptionItemDto>>

    fun add(entity: CountryBaseInfoEntity)

    fun addAll(list: List<CountryBaseInfoEntity>)
}