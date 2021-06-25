package com.example.course_android

class CountriesRootObject (
    val countriesObject: List<CountriesObject>
        )

class CountriesObject (
    val name: String,
    val topLevelDomain: List<String?>,
    val alpha2Code: String,
    val alpha3Code: String,
    val callingCode: List<String?>,
    val capital: String,
    val altSpellings: List<String?>,
    val region: String,
    val subregion: String,
    val population: Int,
    val latlng: List<Float>,
    val demonym: String,
    val area: Float,
    val gini: Float,
    val timezones: List<String>,
    var borders: List<String>,
    var nativeName: String,
    var numericCode: String,
    val currencies: List<Currencies>,
    val languages: List<Languages>,
    val translations: Translations,
    val flag: String,
    val regionalBlocs: List<RegionalBlocs>
)

class Currencies (
    val code: String,
    val name: String,
    val symbol: String
    )

class Languages (
    val iso639_1: String,
    val iso639_2: String,
    val name: String,
    val nativeName: String
        )

class Translations (
    val de: String,
    val es: String,
    val fr: String,
    val ja: String,
    val it: String,
    val br: String,
    val pt: String,
    val nl: String,
    val hr: String,
    val fa: String
)

class RegionalBlocs (
    val acronym: String,
    val name: String,
    val otherAcronyms: List<String?>,
    val otherNames: List<String?>
)