package com.example.course_android.di.dagger.module

import android.content.Context
import com.example.course_android.CountriesApp
import com.example.data.api.NewsFlowService
import com.example.data.ext.ListArticlesToListArticlesDtoTransformer
import com.example.data.repository.flow.NetworkNewsRepositoryImpl
import com.example.domain.repository.NetworkNewsFlowRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    fun provideApplicationContext(application: CountriesApp): Context = application.applicationContext

    @Provides
    fun providesTransformerComponent(): ListArticlesToListArticlesDtoTransformer = ListArticlesToListArticlesDtoTransformer()

    @Provides
    @Singleton
    fun provideNetworkNewsFlowRepositoryAccessor(
        apiService: NewsFlowService,
        transformer: ListArticlesToListArticlesDtoTransformer
    ): NetworkNewsFlowRepository =
        NetworkNewsRepositoryImpl(apiService, transformer)

}