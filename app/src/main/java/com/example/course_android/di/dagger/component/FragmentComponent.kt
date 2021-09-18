package com.example.course_android.di.dagger.component

import com.example.course_android.base.mvi.RootBaseFragment
import com.example.course_android.di.dagger.annotations.FragmentScope
import com.example.course_android.di.dagger.common.AppRouter
import com.example.course_android.di.dagger.module.FragmentModule
import com.example.domain.usecase.impl.GetNewsByNameOutcomeFlowUseCase
import dagger.Component
import dagger.Module
import dagger.Provides

@FragmentScope
@Component(modules = [FragmentModule::class], dependencies = [AppComponent::class])
interface FragmentComponent {
    fun inject(baseFragment: RootBaseFragment)
    fun appRouter(): AppRouter
}