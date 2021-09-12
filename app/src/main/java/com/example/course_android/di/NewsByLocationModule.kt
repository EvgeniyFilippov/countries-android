package com.example.course_android.di

import androidx.lifecycle.SavedStateHandle
import com.example.course_android.fragments.allCountries.AllCountriesViewModel
import com.example.course_android.fragments.newsByLocation.NewsByLocationFragment
import com.example.course_android.fragments.newsByLocation.NewsByLocationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newsByLocationModule = module {

    scope<NewsByLocationFragment> {

        viewModel { (handle: SavedStateHandle) -> NewsByLocationViewModel(handle) }
    }
}