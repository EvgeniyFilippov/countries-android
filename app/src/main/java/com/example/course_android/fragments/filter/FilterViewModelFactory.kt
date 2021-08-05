package com.example.course_android.fragments.filter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.rxjava3.subjects.BehaviorSubject

class FilterViewModelFactory(
    savedStateHandle: SavedStateHandle
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilterViewModel::class.java)) {
            return FilterViewModel(savedStateHandle = SavedStateHandle()) as T
        }
        throw IllegalArgumentException("Error class. Get ${modelClass.canonicalName}, FilterViewModel")
    }
}