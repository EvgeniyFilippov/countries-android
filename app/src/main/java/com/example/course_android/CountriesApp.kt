package com.example.course_android

import android.app.Application
import com.example.data.api.RetrofitObj
import com.example.course_android.di.*
import com.example.data.room.CountryInfoDAO
import com.example.data.room.DatabaseInfo
import com.example.data.room.LanguagesInfoDAO
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import retrofit2.Retrofit

class CountriesApp : Application() {

    companion object {
        lateinit var retrofit: Retrofit
//        var base: DatabaseInfo? = null
//        var daoCountry: CountryInfoDAO? = null
//        var daoLanguage: LanguagesInfoDAO? = null
    }

    override fun onCreate() {
        super.onCreate()
        retrofit = com.example.data.api.RetrofitObj.getRetrofit()
//        base = this.let { DatabaseInfo.init(it) }
//        daoCountry = base?.getCountryInfoDAO()
//        daoLanguage = base?.getLanguageInfoDAO()
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
                filterModule
            )
        }

    }
}