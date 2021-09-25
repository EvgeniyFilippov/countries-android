package com.example.course_android.di.dagger.module

import com.example.data.api.NewsFlowService
import com.example.data.api.RetrofitCreator
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun getNewsFlowService(): NewsFlowService {
        val creator = RetrofitCreator()
        return creator.createFlowNewsService(NewsFlowService::class.java)
    }

}