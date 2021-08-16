package com.example.course_android.di

import com.example.course_android.api.RetrofitObj
import com.example.course_android.room.DatabaseInfo
import com.example.domain.repository.DatabaseCountryRepository
import com.repository.database.DatabaseCountryRepositoryImpl
import com.example.domain.repository.DatabaseLanguageRepository
import com.repository.database.DatabaseLanguageRepositoryImpl
import com.example.domain.repository.NetworkRepository
import com.repository.network.NetworkRepositoryImpl
import org.koin.dsl.module

val appModule = module {

    //Model level
    single { DatabaseInfo.init(get()) }
    single { RetrofitObj.getCountriesApi() }

    //Data level
    single { NetworkRepositoryImpl(get()) as com.example.domain.repository.NetworkRepository }
    single { DatabaseCountryRepositoryImpl(get()) as com.example.domain.repository.DatabaseCountryRepository }
    single { DatabaseLanguageRepositoryImpl(get()) as com.example.domain.repository.DatabaseLanguageRepository }

}