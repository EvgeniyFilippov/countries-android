package com.example.course_android.di

import com.example.data.api.RetrofitObj
import com.example.data.repository.database.DatabaseCountryRepositoryImpl
import com.example.data.repository.database.DatabaseLanguageRepositoryImpl
import com.example.data.repository.network.NetworkCapitalRepositoryImpl
import com.example.data.repository.network.NetworkRepositoryImpl
import com.example.data.room.DatabaseInfo
import com.example.domain.repository.DatabaseCountryRepository
import com.example.domain.repository.DatabaseLanguageRepository
import com.example.domain.repository.NetworkCapitalsRepository
import com.example.domain.repository.NetworkRepository
import org.koin.dsl.module

val appModule = module {

    //Model level
    single { DatabaseInfo.init(get()) }
    single { RetrofitObj.getCountriesApi() }

    //Data level
    single { NetworkRepositoryImpl(get()) as NetworkRepository }
    single { NetworkCapitalRepositoryImpl(get()) as NetworkCapitalsRepository }
    single { DatabaseCountryRepositoryImpl(get()) as DatabaseCountryRepository }
    single { DatabaseLanguageRepositoryImpl(get()) as DatabaseLanguageRepository }

}