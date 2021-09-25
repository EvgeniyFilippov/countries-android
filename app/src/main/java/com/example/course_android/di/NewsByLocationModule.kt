package com.example.course_android.di

import com.example.course_android.fragments.newsByLocation.NewsByLocationFragment
import com.example.course_android.fragments.newsByLocation.NewsByLocationViewModel
import com.example.domain.usecase.impl.GetNewsByNameOutcomeFlowUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newsByLocationModule = module {

    scope<NewsByLocationFragment> {

        scoped { GetNewsByNameOutcomeFlowUseCase(get()) }

        viewModel { NewsByLocationViewModel(get(), get()) }
    }
}