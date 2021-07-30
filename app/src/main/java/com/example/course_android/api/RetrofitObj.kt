package com.example.course_android.api

import com.example.course_android.Constants
import com.example.course_android.CountriesApp
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitObj {

    private val loggingInterceptor = HttpLoggingInterceptor()
    private val okHttpClient = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    fun getRetrofit(): Retrofit {
      return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    fun getCountriesApi(): CountriesApi {
        return CountriesApp.retrofit.create(CountriesApi::class.java)
    }

    fun getCountryDescriptionApi(): CountryDescriptionApi {
        return CountriesApp.retrofit.create(CountryDescriptionApi::class.java)
    }



    init {
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }
}