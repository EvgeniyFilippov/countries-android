package com.example.course_android.fragments.allCountries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AllCountriesViewModelFactory(
    private var sortStatus:  Int
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllCountriesViewModel::class.java)) {
            return AllCountriesViewModel(sortStatus) as T
        }
        throw IllegalArgumentException("Error class. Get ${modelClass.canonicalName}, required AllCountriesViewModel")
    }
}