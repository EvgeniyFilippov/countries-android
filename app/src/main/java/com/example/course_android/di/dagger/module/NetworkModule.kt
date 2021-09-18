package com.example.course_android.di.dagger.module

import android.content.Context
import com.chenxyu.retrofit.adapter.FlowCallAdapterFactory
import com.example.data.NetConstants.BASE_URL_NEWS
import com.example.data.NetConstants.SESSION_TIMEOUT
import com.example.data.api.CoroutineCountryService
import com.example.data.api.CountryService
import com.example.data.api.NewsFlowService
import com.example.data.api.RetrofitCreator
import com.google.android.gms.common.internal.service.Common

import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class NetworkModule {

//    @Provides
//    @Singleton
//    fun providesOkhttpCache(context: Context): Cache =
//        Cache(context.cacheDir, 1024)
//
//    @Provides
//    @Singleton
//    fun providesOkhttpClient(cache: Cache): OkHttpClient {
//        val loggingInterceptor = HttpLoggingInterceptor()
//        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
//
//        val client = OkHttpClient.Builder()
//            .cache(cache)
//            .connectTimeout(30000, TimeUnit.MILLISECONDS)
//            .readTimeout(30000, TimeUnit.MILLISECONDS)
//            .addInterceptor(loggingInterceptor)
//
//        return client.build()
//    }
//
//    @Provides
//    @Singleton
//    fun providesRetrofitFlow(okHttpClient: OkHttpClient): Retrofit =
//        Retrofit.Builder()
//            .baseUrl(BASE_URL_NEWS)
//            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(FlowCallAdapterFactory())
//            .client(okHttpClient)
//            .build()
//
//    @Provides
//    @Singleton
//    fun getFlowClientForNews(retrofit: Retrofit): NewsFlowService =
//        retrofit.create(NewsFlowService::class.java)

    @Provides
    @Singleton
    fun getFlowClientForNews(): NewsFlowService {
        val creator = RetrofitCreator()
        return creator.createFlowNewsService(NewsFlowService::class.java)
    }

}