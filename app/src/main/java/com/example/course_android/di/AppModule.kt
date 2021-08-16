package com.example.course_android.di

import com.example.data.api.RetrofitObj
import com.example.data.room.DatabaseInfo
import com.example.domain.repository.DatabaseCountryRepository
import com.example.data.repository.database.DatabaseCountryRepositoryImpl
import com.example.domain.repository.DatabaseLanguageRepository
import com.example.data.repository.database.DatabaseLanguageRepositoryImpl
import com.example.domain.repository.NetworkRepository
import com.example.data.repository.network.NetworkRepositoryImpl
import org.koin.dsl.module

val appModule = module {

    //Model level
    single { com.example.data.room.DatabaseInfo.init(get()) }
    single { com.example.data.api.RetrofitObj.getCountriesApi() }

    //Data level
    single { com.example.data.repository.network.NetworkRepositoryImpl(get()) as com.example.domain.repository.NetworkRepository }
    single { com.example.data.repository.database.DatabaseCountryRepositoryImpl(get()) as com.example.domain.repository.DatabaseCountryRepository }
    single { com.example.data.repository.database.DatabaseLanguageRepositoryImpl(get()) as com.example.domain.repository.DatabaseLanguageRepository }

}