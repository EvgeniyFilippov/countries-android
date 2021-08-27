package com.example.course_android.di

import com.example.data.api.RetrofitObj
import com.example.data.ext.ListArticlesToListArticlesDtoTransformer
import com.example.data.model.newsByCountry.Article
import com.example.data.repository.database.DatabaseCountryRepositoryImpl
import com.example.data.repository.database.DatabaseLanguageRepositoryImpl
import com.example.data.repository.flow.NetworkNewsRepositoryImpl
import com.example.data.repository.network.NetworkCapitalRepositoryImpl
import com.example.data.repository.network.NetworkRepositoryImpl
import com.example.data.room.DatabaseInfo
import com.example.domain.dto.news.NewsItemDto
import com.example.domain.outcome.Transformer
import com.example.domain.repository.*
import org.koin.dsl.module

val appModule = module {

    //Model level
    single { DatabaseInfo.init(get()) }
    single { RetrofitObj.getCountriesApi() }
    single { RetrofitObj.getCapitalsApi() }
    single { RetrofitObj.getNewsApi() }

    //Data level
    single { NetworkRepositoryImpl(get()) as NetworkRepository }
    single { NetworkCapitalRepositoryImpl(get()) as NetworkCapitalsRepository }

    single<Transformer<List<Article>, List<NewsItemDto>>> { ListArticlesToListArticlesDtoTransformer() }
    single<NetworkNewsFlowRepository> { NetworkNewsRepositoryImpl(get(), get()) }

    single { DatabaseCountryRepositoryImpl(get()) as DatabaseCountryRepository }
    single { DatabaseLanguageRepositoryImpl(get()) as DatabaseLanguageRepository }

}