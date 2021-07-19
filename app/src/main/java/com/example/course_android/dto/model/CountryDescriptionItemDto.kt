package com.example.course_android.dto.model

import com.example.course_android.model.allCountries.Language
import com.example.course_android.model.oneCountry.LanguageOfOneCountry

data class CountryDescriptionItemDto(
//    val alpha2Code: String,
//    val alpha3Code: String,
//    val altSpellings: List<String>,
//    val area: Double,
//    val borders: List<String>,
//    val callingCodes: List<String>,
//    val capital: String,
//    val cioc: String,
//    val currencies: List<Currency>,
//    val demonym: String,
    var flag: String = "",
//    val gini: Double,
    var languages: MutableList<LanguageOfOneCountryDto> = mutableListOf(),
    var latlng: List<Double> = arrayListOf(1.0, 1.0),
    var name: String = ""
//    val nativeName: String,
//    val numericCode: String,
//    val population: Int,
//    val region: String,
//    val regionalBlocs: List<RegionalBloc>,
//    val subregion: String,
//    val timezones: List<String>,
//    val topLevelDomain: List<String>,
//    val translations: Translations
)