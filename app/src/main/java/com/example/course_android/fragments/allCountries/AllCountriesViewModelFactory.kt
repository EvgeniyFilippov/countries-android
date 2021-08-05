package com.example.course_android.fragments.allCountries

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

class AllCountriesViewModelFactory(
    private var sortStatus:  Int,
    private val mSearchSubject: PublishSubject<String>,
    savedStateHandle: SavedStateHandle
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllCountriesViewModel::class.java)) {
            return AllCountriesViewModel(sortStatus, mSearchSubject, savedStateHandle = SavedStateHandle()) as T
        }
        throw IllegalArgumentException("Error class. Get ${modelClass.canonicalName}, required AllCountriesViewModel")
    }
}