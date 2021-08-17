package com.example.course_android.dto.model

import java.io.Serializable

data class LanguageOfOneCountryDto (
    var iso639_1: String = "",
    var iso639_2: String = "",
    var name: String = "",
    var nativeName: String = ""
        ) : Serializable
