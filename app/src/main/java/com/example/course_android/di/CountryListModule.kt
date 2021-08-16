package com.example.course_android.di

import androidx.lifecycle.SavedStateHandle
import com.example.course_android.fragments.allCountries.AllCountriesFragment
import com.example.course_android.fragments.allCountries.AllCountriesViewModel
import com.example.domain.usecase.impl.GetAllCountriesUseCase
import com.example.domain.usecase.impl.GetCountryListByNameUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val countryListModule = module {

  scope<AllCountriesFragment> {

      scoped { GetAllCountriesUseCase(get()) }
      scoped { GetCountryListByNameUseCase(get()) }

      viewModel { (handle: SavedStateHandle) -> AllCountriesViewModel(handle, get(), get(), get(), get()) }
  }
}