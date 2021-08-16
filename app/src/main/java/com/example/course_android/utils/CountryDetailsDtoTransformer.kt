package com.example.course_android.utils

import com.example.course_android.Constants.DEFAULT_DOUBLE
import com.example.course_android.Constants.DEFAULT_FLAG
import com.example.course_android.Constants.DEFAULT_INT
import com.example.course_android.Constants.DEFAULT_LATLNG_SIZE
import com.example.course_android.Constants.DEFAULT_STRING
import com.example.domain.dto.model.CountryDescriptionItemDto
import com.example.domain.dto.model.LanguageOfOneCountryDto
import com.example.data.model.oneCountry.CountryDescriptionItem

   fun MutableList<com.example.data.model.oneCountry.CountryDescriptionItem>?.transformCountryToDto(): MutableList<CountryDescriptionItemDto> {

        val listCountryDescriptionItemDto: MutableList<CountryDescriptionItemDto> = mutableListOf()


        this?.forEach { country ->
            val countryDescriptionItemDto = CountryDescriptionItemDto()
            val listOfLanguagesDto: MutableList<LanguageOfOneCountryDto> = mutableListOf()

            countryDescriptionItemDto.flag = country.flag ?: DEFAULT_FLAG
            countryDescriptionItemDto.name = country.name ?: DEFAULT_STRING
            countryDescriptionItemDto.area = country.area ?: DEFAULT_DOUBLE
            countryDescriptionItemDto.capital = country.capital ?: DEFAULT_STRING
            countryDescriptionItemDto.population = country.population ?: DEFAULT_INT

            countryDescriptionItemDto.latlng =
                country.latlng ?: arrayListOf(DEFAULT_DOUBLE, DEFAULT_DOUBLE)
            if (countryDescriptionItemDto.latlng.size != DEFAULT_LATLNG_SIZE) {
                countryDescriptionItemDto.latlng = arrayListOf(DEFAULT_DOUBLE, DEFAULT_DOUBLE)
            }

            var count = 0;
            country.languages?.forEach {
                val languageDto = LanguageOfOneCountryDto()
                languageDto.iso639_1 = it.iso639_1 ?: ""
                languageDto.iso639_2 = it.iso639_2 ?: ""
                languageDto.name = it.name ?: ""
                languageDto.nativeName = it.nativeName ?: ""
                listOfLanguagesDto.add(languageDto)
                count++
            }
            countryDescriptionItemDto.languages = listOfLanguagesDto
            listCountryDescriptionItemDto.add(countryDescriptionItemDto)
        }

        return listCountryDescriptionItemDto

}