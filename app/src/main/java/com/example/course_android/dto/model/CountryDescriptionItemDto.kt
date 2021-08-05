package com.example.course_android.dto.model

data class CountryDescriptionItemDto(
//    val alpha2Code: String,
//    val alpha3Code: String,
//    val altSpellings: List<String>,
    var area: Double = 0.0,
//    val borders: List<String>,
//    val callingCodes: List<String>,
    var capital: String = "",
//    val cioc: String,
//    val currencies: List<Currency>,
//    val demonym: String,
    var flag: String = "",
//    val gini: Double,
    var languages: MutableList<LanguageOfOneCountryDto> = mutableListOf(),
    var latlng: List<Double> = arrayListOf(1.0, 1.0),
    var name: String = "",
//    val nativeName: String,
//    val numericCode: String,
    var population: Int = 0
//    val region: String,
//    val regionalBlocs: List<RegionalBloc>,
//    val subregion: String,
//    val timezones: List<String>,
//    val topLevelDomain: List<String>,
//    val translations: Translations
)