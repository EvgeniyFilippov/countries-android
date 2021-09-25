package com.example.course_android.di.dagger.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.course_android.di.dagger.annotations.ViewModelKey
import com.example.course_android.fragments.newsByLocation.NewsByLocationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NewsByLocationViewModel::class)
    abstract fun bindNewsViewModel(newsViewModel: NewsByLocationViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}