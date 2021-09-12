package com.example.course_android.di

import com.example.course_android.fragments.newsByLocation.NewsByLocationFragment
import org.koin.dsl.module

val newsByLocationModule = module {

    scope<NewsByLocationFragment> {

    }
}