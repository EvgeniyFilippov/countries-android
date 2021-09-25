package com.example.course_android.di.koin

import androidx.lifecycle.SavedStateHandle
import com.example.course_android.fragments.allCapitals.AllCapitalsFragment
import com.example.course_android.fragments.allCapitals.AllCapitalsViewModel
import com.example.domain.usecase.impl.GetCapitalsUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val capitalListModule = module {

  scope<AllCapitalsFragment> {

      scoped { GetCapitalsUseCase(get()) }

      viewModel { (handle: SavedStateHandle) -> AllCapitalsViewModel(handle, get()) }
  }
}