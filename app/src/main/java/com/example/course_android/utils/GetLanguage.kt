package com.example.course_android.utils

import com.example.course_android.model.CountriesDataItem

fun getLanguageByKey(countriesList: List<CountriesDataItem>, position: Int): StringBuilder {
    val myStringBuilder = StringBuilder()
    for (n in countriesList[position].languages.indices) {
        myStringBuilder.append(countriesList[position].languages.get(n).name)
        if (n < countriesList[position].languages.size - 1) {
            myStringBuilder.append(", ")
        }
    }
    return myStringBuilder
}

