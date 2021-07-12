package com.example.course_android.utils

import com.example.course_android.model.allCountries.Language

fun List<Language>.getLanguageByKey(): StringBuilder {
    val myStringBuilder = StringBuilder()
    for (n in this.indices) {
        myStringBuilder.append(this[n].name)
        if (n < this.size - 1) {
            myStringBuilder.append(", ")
        }
    }
    return myStringBuilder
}

