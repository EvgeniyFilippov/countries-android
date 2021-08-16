package com.example.course_android.utils

import com.example.course_android.Constants
import com.example.domain.dto.room.RoomCountryDescriptionItemDto
import com.example.data.room.CountryBaseInfoEntity

fun List<com.example.data.room.CountryBaseInfoEntity>.convertCountryEntityToDto(): MutableList<RoomCountryDescriptionItemDto> {

    val listCountryEntityDto: MutableList<RoomCountryDescriptionItemDto> = mutableListOf()

    this.forEach {
        val countryEntityDto = RoomCountryDescriptionItemDto()
        countryEntityDto.area = it.area ?: Constants.DEFAULT_DOUBLE
        countryEntityDto.capital = it.capital ?: Constants.DEFAULT_STRING
        countryEntityDto.name = it.name ?: Constants.DEFAULT_STRING

        listCountryEntityDto.add(countryEntityDto)
    }
    return listCountryEntityDto

}