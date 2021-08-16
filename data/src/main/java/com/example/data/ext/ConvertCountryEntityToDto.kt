package com.example.data.ext

import com.example.data.NetConstants.DEFAULT_DOUBLE
import com.example.data.NetConstants.DEFAULT_STRING
import com.example.domain.dto.room.RoomCountryDescriptionItemDto
import com.example.data.room.CountryBaseInfoEntity

fun List<CountryBaseInfoEntity>.convertCountryEntityToDto(): MutableList<RoomCountryDescriptionItemDto> {

    val listCountryEntityDto: MutableList<RoomCountryDescriptionItemDto> = mutableListOf()

    this.forEach {
        val countryEntityDto = RoomCountryDescriptionItemDto()
        countryEntityDto.area = it.area ?: DEFAULT_DOUBLE
        countryEntityDto.capital = it.capital ?: DEFAULT_STRING
        countryEntityDto.name = it.name ?: DEFAULT_STRING

        listCountryEntityDto.add(countryEntityDto)
    }
    return listCountryEntityDto

}