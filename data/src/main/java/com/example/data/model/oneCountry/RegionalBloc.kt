package com.example.data.model.oneCountry

data class RegionalBloc(
    val acronym: String,
    val name: String,
    val otherAcronyms: List<String>,
    val otherNames: List<Any>
)