package com.example.course_android.model.oneCountry

data class RegionalBloc(
    val acronym: String,
    val name: String,
    val otherAcronyms: List<String>,
    val otherNames: List<Any>
)