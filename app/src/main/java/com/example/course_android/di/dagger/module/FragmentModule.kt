package com.example.course_android.di.dagger.module

import android.content.Context
import com.example.course_android.base.mvi.RootBaseFragment
import com.example.course_android.di.dagger.common.AppRouter
import com.example.data.api.NewsFlowService
import com.example.data.ext.ListArticlesToListArticlesDtoTransformer
import com.example.data.repository.flow.NetworkNewsRepositoryImpl
import com.example.domain.repository.NetworkNewsFlowRepository
import com.example.domain.usecase.impl.GetNewsByNameOutcomeFlowUseCase
import dagger.Module
import dagger.Provides

@Module
class FragmentModule constructor(private val fragment : RootBaseFragment) {

    @Provides
    fun providesFragmentContext() : Context = fragment.requireContext()

    @Provides
    fun providesFragment(): RootBaseFragment {
        return fragment
    }

    @Provides
    fun providesRouterComponent(): AppRouter = AppRouter(fragment)

    @Provides
    fun providesTransformerComponent(): ListArticlesToListArticlesDtoTransformer = ListArticlesToListArticlesDtoTransformer()

    @Provides
    fun providesNetworkNewsFlowRepository(
        apiService: NewsFlowService,
        transformer: ListArticlesToListArticlesDtoTransformer
    ): NetworkNewsFlowRepository = NetworkNewsRepositoryImpl(apiService, transformer)

    @Provides
    fun providesNetworkNewsFlowUseCase(
        repository: NetworkNewsFlowRepository
    ): GetNewsByNameOutcomeFlowUseCase = GetNewsByNameOutcomeFlowUseCase(repository)

}