package com.example.course_android.di.koin

import androidx.lifecycle.SavedStateHandle
import com.example.course_android.fragments.news.NewsFragment
import com.example.course_android.fragments.news.NewsViewModel
import com.example.domain.usecase.impl.GetNewsByNameFlowUseCase
import com.example.domain.usecase.impl.GetNewsByNameOutcomeFlowUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newsModule = module {

    scope<NewsFragment> {

        scoped { GetNewsByNameFlowUseCase(get()) }
        scoped { GetNewsByNameOutcomeFlowUseCase(get()) }

        viewModel { (handle: SavedStateHandle) -> NewsViewModel(handle, get(), get()) }
    }
}