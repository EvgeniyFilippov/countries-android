package com.repository.database

import com.example.course_android.dto.room.RoomLanguageOfOneCountryDto
import com.example.course_android.room.LanguagesInfoEntity
import io.reactivex.rxjava3.core.Flowable

interface DatabaseLanguageRepository {

    fun add(entity: LanguagesInfoEntity)

    fun getAllInfo(): Flowable<List<RoomLanguageOfOneCountryDto>>

    fun getLanguageByCountry(name: String): List<String>

    fun addAll(list: List<LanguagesInfoEntity>)
}