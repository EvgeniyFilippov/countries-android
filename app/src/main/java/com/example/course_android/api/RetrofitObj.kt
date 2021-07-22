package com.example.course_android.api

import com.example.course_android.Constants
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitObj {

    private lateinit var retrofit: Retrofit
    lateinit var okHttpClientBuilder: OkHttpClient.Builder
    lateinit var logging: HttpLoggingInterceptor

    fun getRetrofit(okHttp: OkHttpClient.Builder): Retrofit {
        retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttp.build())
            .build()
        return retrofit
    }

    fun getOkHttp(): OkHttpClient.Builder {
        okHttpClientBuilder = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
        logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        okHttpClientBuilder.addInterceptor(logging)
        return okHttpClientBuilder
    }
}