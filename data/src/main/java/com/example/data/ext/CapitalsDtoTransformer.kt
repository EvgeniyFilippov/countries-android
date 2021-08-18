package com.example.data.ext

import com.example.data.NetConstants.DEFAULT_STRING
import com.example.data.model.capitals.CapitalItem
import com.example.domain.dto.model.CapitalItemDto

fun MutableList<CapitalItem>?.transformCapitalToDto(): MutableList<CapitalItemDto> {

    val listCapitalsItemDto: MutableList<CapitalItemDto> = mutableListOf()

    this?.forEach { capital ->
        val capitalItemDto = CapitalItemDto()
        capitalItemDto.capital = capital.capital ?: DEFAULT_STRING
        listCapitalsItemDto.add(capitalItemDto)
    }

    return listCapitalsItemDto
}