package com.example.course_android.base.mvi

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.course_android.CountriesApp
import com.example.course_android.di.dagger.common.AppRouter
import com.example.course_android.di.dagger.component.DaggerFragmentComponent
import com.example.course_android.di.dagger.component.FragmentComponent
import com.example.course_android.di.dagger.module.FragmentModule
import com.example.course_android.di.dagger.viewmodels.DaggerViewModelFactory
import com.example.data.ext.ListArticlesToListArticlesDtoTransformer
import javax.inject.Inject

open class RootBaseFragment: Fragment() {

    private val fragmentComponent: FragmentComponent by lazy {
        DaggerFragmentComponent.builder().fragmentModule(FragmentModule(this))
            .appComponent(CountriesApp.appComponents).build()
    }

    @Inject
    lateinit var listArticlesToListArticlesDtoTransformer: ListArticlesToListArticlesDtoTransformer

    @Inject
    lateinit var appRouter: AppRouter

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

}