package com.example.course_android.di

import com.example.course_android.api.RetrofitObj
import com.example.course_android.room.DatabaseInfo
import com.repository.network.NetworkRepository
import com.repository.network.NetworkRepositoryImpl
import org.koin.dsl.module

val appModule = module {

    //Model level
    single { DatabaseInfo.init(get()) }
    single { RetrofitObj.getCountriesApi()}
    //Data level
    single { NetworkRepositoryImpl(get()) as NetworkRepository}

}