package com.example.course_android.utils

import com.example.course_android.Constants
import com.example.course_android.model.allCountries.CountriesDataItem
import com.example.course_android.model.allCountries.Language
import com.example.course_android.room.CountryBaseInfoEntity
import com.example.course_android.room.LanguagesInfoDAO

fun List<CountryBaseInfoEntity>?.convertDBdataToRetrofitModel(
    languagesFromDB: LanguagesInfoDAO?, listOfCountriesFromDB: MutableList<CountriesDataItem>
): MutableList<CountriesDataItem> {
    this?.forEach { countryDB ->
        val listOfLanguagesFromDB: MutableList<Language> = mutableListOf()
        languagesFromDB?.getLanguageByCountry(countryDB.name)?.forEach { languageDB ->
            val languageItem = Language(
                Constants.DEFAULT_STRING,
                Constants.DEFAULT_STRING,
                languageDB,
                Constants.DEFAULT_STRING
            )
            listOfLanguagesFromDB.add(languageItem)
        }
        val countryDataItem = CountriesDataItem(
            countryDB.area,
            countryDB.capital,
            listOfLanguagesFromDB,
            listOf(0.0, 0.0),
            countryDB.name
        )
        listOfCountriesFromDB.add(countryDataItem)
    }
    return listOfCountriesFromDB
}