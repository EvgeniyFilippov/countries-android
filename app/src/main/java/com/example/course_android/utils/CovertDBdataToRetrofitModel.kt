package com.example.course_android.utils

import com.example.course_android.Constants.DEFAULT_DOUBLE
import com.example.course_android.Constants.DEFAULT_STRING
import com.example.course_android.dto.model.CountryDescriptionItemDto
import com.example.course_android.dto.model.LanguageOfOneCountryDto
import com.example.course_android.room.CountryBaseInfoEntity
import com.example.course_android.room.LanguagesInfoDAO
import com.repository.database.DatabaseLanguageRepository

fun List<CountryBaseInfoEntity>?.convertDBdataToRetrofitModel(
    languagesFromDB: DatabaseLanguageRepository, listOfCountriesFromDB: MutableList<CountryDescriptionItemDto>
): MutableList<CountryDescriptionItemDto> {
    this?.forEach { countryDB ->
        val listOfLanguagesFromDB: MutableList<LanguageOfOneCountryDto> = mutableListOf()
        languagesFromDB?.getLanguageByCountry(countryDB.name)?.forEach { languageDB ->
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