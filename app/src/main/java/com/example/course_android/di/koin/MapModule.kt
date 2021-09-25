package com.example.course_android.di.koin

import com.example.course_android.fragments.map.MapAllCountriesFragment
import com.example.course_android.fragments.map.MapAllCountriesPresenter
import com.example.domain.usecase.impl.GetAllCountriesUseCase
import org.koin.dsl.module

val mapModule = module{

    scope<MapAllCountriesFragment> {
        scoped { MapAllCountriesPresenter(get()) }
        scoped { GetAllCountriesUseCase(get()) }
    }

}