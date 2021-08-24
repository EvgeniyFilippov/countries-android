package com.example.course_android.di

import com.example.course_android.fragments.details.CountryDetailsFragment
import com.example.course_android.fragments.details.CountryDetailsPresenter
import com.example.domain.usecase.impl.GetCountryListByNameUseCase
import org.koin.dsl.module

val countryDetailsModule = module {

    scope<CountryDetailsFragment> {
        scoped { CountryDetailsPresenter(get()) }
        scoped { GetCountryListByNameUseCase(get()) }
    }
}