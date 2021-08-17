package com.example.course_android.di

import androidx.lifecycle.SavedStateHandle
import com.example.course_android.fragments.allCountries.AllCountriesFragment
import com.example.course_android.fragments.allCountries.AllCountriesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val countryListModule = module {

  scope<AllCountriesFragment> {
      viewModel { (handle: SavedStateHandle) -> AllCountriesViewModel(handle, get(), get(), get()) }
  }
}