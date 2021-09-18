package com.example.course_android.di.dagger.component

import com.example.course_android.CountriesApp
import com.example.course_android.di.dagger.module.ApplicationModule
import com.example.course_android.di.dagger.module.NetworkModule
import com.example.course_android.di.dagger.viewmodels.DaggerViewModelFactory
import com.example.course_android.di.dagger.viewmodels.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ApplicationModule::class, NetworkModule::class, ViewModelModule::class])
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(app: CountriesApp): Builder
    }

    fun provideDaggerViewModuleFactory(): DaggerViewModelFactory
}