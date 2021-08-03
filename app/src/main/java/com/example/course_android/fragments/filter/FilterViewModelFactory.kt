package com.example.course_android.fragments.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.rxjava3.subjects.BehaviorSubject

class FilterViewModelFactory(
//    private var start:  Float,
//    private val end: Float
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilterViewModel::class.java)) {
            return FilterViewModel() as T
        }
        throw IllegalArgumentException("Error class. Get ${modelClass.canonicalName}, FilterViewModel")
    }
}