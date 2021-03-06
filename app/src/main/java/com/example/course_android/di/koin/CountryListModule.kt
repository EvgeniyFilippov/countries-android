package com.example.course_android.di.koin

import androidx.lifecycle.SavedStateHandle
import com.example.course_android.fragments.allCountries.AllCountriesFragment
import com.example.course_android.fragments.allCountries.AllCountriesViewModel
import com.example.domain.usecase.impl.GetAllCountriesUseCase
import com.example.domain.usecase.impl.GetCountryListByNameUseCase
import com.example.domain.usecase.impl.GetListCountriesFromDbUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val countryListModule = module {

  scope<AllCountriesFragment> {

      scoped { GetAllCountriesUseCase(get()) }
      scoped { GetCountryListByNameUseCase(get()) }
      scoped { GetListCountriesFromDbUseCase(get()) }

      viewModel { (handle: SavedStateHandle) -> AllCountriesViewModel(handle, get(), get(), get(), get(), get()) }
  }
}