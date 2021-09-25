package com.example.course_android

import android.app.Application
import com.example.course_android.di.*
import com.example.course_android.di.dagger.component.AppComponent
import com.example.course_android.di.dagger.component.DaggerAppComponent
import com.example.course_android.di.koin.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class CountriesApp : Application() {

    companion object {
        lateinit var appComponents: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponents = initDaggerDI()
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
                capitalListModule,
                newsModule
            )
        }
    }

    private fun initDaggerDI(): AppComponent =
        DaggerAppComponent.builder().application(this).build()
}