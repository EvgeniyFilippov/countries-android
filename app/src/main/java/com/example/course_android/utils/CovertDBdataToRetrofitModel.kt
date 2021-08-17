package com.example.course_android.utils

import com.example.course_android.Constants.DEFAULT_DOUBLE
import com.example.course_android.Constants.DEFAULT_STRING
import com.example.domain.dto.model.CountryDescriptionItemDto
import com.example.domain.dto.model.LanguageOfOneCountryDto
import com.example.domain.dto.room.RoomCountryDescriptionItemDto
import com.example.domain.repository.DatabaseLanguageRepository

fun List<RoomCountryDescriptionItemDto>?.convertDBdataToRetrofitModel(
    languagesFromDB: DatabaseLanguageRepository,
    listOfCountriesFromDB: MutableList<CountryDescriptionItemDto>
): MutableList<CountryDescriptionItemDto> {
    this?.forEach { countryDB ->
        val listOfLanguagesFromDB: MutableList<LanguageOfOneCountryDto> = mutableListOf()
        languagesFromDB.getLanguageByCountry(countryDB.name).forEach { languageDB ->
            val languageItem = LanguageOfOneCountryDto(
                DEFAULT_STRING,
                DEFAULT_STRING,
                languageDB,
                DEFAULT_STRING
            )
            listOfLanguagesFromDB.add(languageItem)
        }
        val countryDataItem = CountryDescriptionItemDto(
            countryDB.area,
            countryDB.capital,
            DEFAULT_STRING,
            listOfLanguagesFromDB,
            listOf(DEFAULT_DOUBLE, DEFAULT_DOUBLE),
            countryDB.name
        )
        listOfCountriesFromDB.add(countryDataItem)
    }
    return listOfCountriesFromDB
}