package com.example.course_android.ext

import android.util.Log
import com.example.domain.dto.model.CountryDescriptionItemDto
import com.example.domain.dto.room.RoomCountryDescriptionItemDto
import com.example.domain.dto.room.RoomLanguageOfOneCountryDto
import com.example.domain.repository.DatabaseCountryRepository
import com.example.domain.repository.DatabaseLanguageRepository

fun MutableList<CountryDescriptionItemDto>.convertApiDtoToRoomDto(
    mDatabaseCountryRepository: DatabaseCountryRepository,
    mDatabaseLanguageRepository: DatabaseLanguageRepository
) {
    val listOfAllCountries: MutableList<RoomCountryDescriptionItemDto> = mutableListOf()
    val listOfAllLanguages: MutableList<RoomLanguageOfOneCountryDto> = mutableListOf()
    this.forEach { item ->
        Log.d("HUHUHU", Thread.currentThread().name)
        listOfAllCountries.add(
            RoomCountryDescriptionItemDto(
                item.name,
                item.capital,
                item.area
            )
        )
        item.languages.forEach { language ->
            listOfAllLanguages.add(
                RoomLanguageOfOneCountryDto(
                    item.name,
                    language.name
                )
            )
        }
    }
    mDatabaseCountryRepository.addAll(listOfAllCountries)
    mDatabaseLanguageRepository.addAll(listOfAllLanguages)
}