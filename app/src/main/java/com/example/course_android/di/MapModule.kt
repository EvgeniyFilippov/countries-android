package com.example.course_android.di

import com.example.course_android.fragments.details.CountryDetailsFragment
import com.example.course_android.fragments.details.CountryDetailsPresenter
import com.example.course_android.fragments.map.MapAllCountriesFragment
import com.example.course_android.fragments.map.MapAllCountriesPresenter
import org.koin.dsl.module

val mapModule = module{

    scope<MapAllCountriesFragment> {
        scoped { MapAllCountriesPresenter(get()) }
    }

}