package com.example.course_android

import android.app.Application
import com.example.course_android.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class CountriesApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Koin Android logger
            androidLogger()
            //inject Android context
            androidContext(this@CountriesApp)
            // use modules
            modules(
                appModule,
                countryListModule,
                countryDetailsModule,
                mapModule,
                filterModule,
                capitalListModule
            )
        }

    }
}