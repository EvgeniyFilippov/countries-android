package com.example.data.api

import com.chenxyu.retrofit.adapter.FlowCallAdapterFactory
import com.example.data.NetConstants
import com.example.data.NetConstants.BASE_URL
import com.example.data.NetConstants.BASE_URL_NEWS
import com.example.data.NetConstants.SESSION_TIMEOUT
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitCreator {

    private fun createOkHttpClient(): OkHttpClient {
        val httpLoginInterceptor = HttpLoggingInterceptor()
        httpLoginInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .connectTimeout(SESSION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(SESSION_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(httpLoginInterceptor)
            .build()
    }

    fun <ServiceClass> createFlowableCountriesService(
        serviceClass: Class<ServiceClass>
    ): ServiceClass {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(createOkHttpClient())
            .build()
        return retrofit.create(serviceClass)
    }

    fun <ServiceClass> createCoroutineCountriesService(
        serviceClass: Class<ServiceClass>
    ): ServiceClass {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .client(createOkHttpClient())
            .build()
        return retrofit.create(serviceClass)
    }

    fun <ServiceClass> createFlowNewsService(
        serviceClass: Class<ServiceClass>
    ): ServiceClass {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL_NEWS)
            .addCallAdapterFactory(FlowCallAdapterFactory())
            .client(createOkHttpClient())
            .build()
        return retrofit.create(serviceClass)
    }

    fun <ServiceClass> createFlowableNewsService(
        serviceClass: Class<ServiceClass>
    ): ServiceClass {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL_NEWS)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(createOkHttpClient())
            .build()
        return retrofit.create(serviceClass)
    }

}