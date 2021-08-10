package com.example.course_android.di

import androidx.lifecycle.SavedStateHandle
import com.example.course_android.fragments.allCountries.AllCountriesFragment
import com.example.course_android.fragments.allCountries.AllCountriesViewModel
import com.example.course_android.fragments.filter.FilterFragment
import com.example.course_android.fragments.filter.FilterViewModel
import com.example.course_android.fragments.map.MapAllCountriesFragment
import com.example.course_android.fragments.map.MapAllCountriesPresenter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val filterModule = module {

    scope<FilterFragment> {
        viewModel { (handle: SavedStateHandle) -> FilterViewModel(handle, get()) }
    }
}