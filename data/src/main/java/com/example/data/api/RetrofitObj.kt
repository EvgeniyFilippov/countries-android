package com.example.data.api

import com.chenxyu.retrofit.adapter.FlowCallAdapterFactory
import com.example.data.NetConstants.BASE_URL
import com.example.data.NetConstants.BASE_URL_NEWS
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
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

    private val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(okHttpClient)
        .build()

    private val coroutineRetrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
        .client(okHttpClient)
        .build()

    private val flowRetrofitBuildr = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(FlowCallAdapterFactory())
        .client(okHttpClient)
        .baseUrl(BASE_URL_NEWS)
        .build()

    val COUNTRY_SERVICE: CountryService = retrofitBuilder.create(CountryService::class.java)
    val COROUTINE_COUNTRY_SERVICE: CoroutineCountryService = coroutineRetrofitBuilder.create(CoroutineCountryService::class.java)
    val FLOW_NEWS_SERVICE: NewsFlowService =
        flowRetrofitBuildr.create(NewsFlowService::class.java)

    fun getCountriesApi(): CountryService = COUNTRY_SERVICE
    fun getCapitalsApi(): CoroutineCountryService = COROUTINE_COUNTRY_SERVICE
    fun getNewsApi(): NewsFlowService = FLOW_NEWS_SERVICE

    init {
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }
}