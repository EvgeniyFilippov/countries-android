package com.example.course_android.utils

import com.example.course_android.Constants
import com.example.course_android.dto.room.RoomLanguageOfOneCountryDto
import com.example.course_android.room.LanguagesInfoEntity

fun List<LanguagesInfoEntity>.convertLanguageEntityToDto(): MutableList<RoomLanguageOfOneCountryDto> {

    val listLanguageEntityDto: MutableList<RoomLanguageOfOneCountryDto> = mutableListOf()

    this.forEach {
        val languageEntityDto = RoomLanguageOfOneCountryDto()
        it.countryName = it.countryName ?: Constants.DEFAULT_STRING
        it.language = it.language ?: Constants.DEFAULT_STRING
        listLanguageEntityDto.add(languageEntityDto)
    }
    return listLanguageEntityDto

}