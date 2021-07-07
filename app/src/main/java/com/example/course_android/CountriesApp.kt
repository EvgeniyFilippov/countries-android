package com.example.course_android

import android.app.Application
import com.example.course_android.api.RetrofitObj
import retrofit2.Retrofit

class CountriesApp : Application() {

    companion object {
        lateinit var retrofit: Retrofit
    }

    override fun onCreate() {
        super.onCreate()
        retrofit = RetrofitObj.getRetrofit(RetrofitObj.getOkHttp())
    }
}

