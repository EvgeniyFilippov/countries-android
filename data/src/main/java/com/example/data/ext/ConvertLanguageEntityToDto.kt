package com.example.data.ext

import com.example.data.NetConstants.DEFAULT_STRING
import com.example.data.room.LanguagesInfoEntity
import com.example.domain.dto.room.RoomLanguageOfOneCountryDto

fun List<LanguagesInfoEntity>.convertLanguageEntityToDto(): MutableList<RoomLanguageOfOneCountryDto> {

    val listLanguageEntityDto: MutableList<RoomLanguageOfOneCountryDto> = mutableListOf()

    this.forEach {
        val languageEntityDto = RoomLanguageOfOneCountryDto()
        it.countryName = it.countryName ?: DEFAULT_STRING
        it.language = it.language ?: DEFAULT_STRING
        listLanguageEntityDto.add(languageEntityDto)
    }
    return listLanguageEntityDto

}

fun List<RoomLanguageOfOneCountryDto>.convertLanguageDtoToEntity(): MutableList<LanguagesInfoEntity> {

    val listLanguageEntity: MutableList<LanguagesInfoEntity> = mutableListOf()

    this.forEach {
        val languageEntity = LanguagesInfoEntity(it.countryName, it.language)
        listLanguageEntity.add(languageEntity)
    }
    return listLanguageEntity

}