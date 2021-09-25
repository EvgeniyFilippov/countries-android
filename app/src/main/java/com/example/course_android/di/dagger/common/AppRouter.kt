package com.example.course_android.di.dagger.common

import com.example.course_android.base.mvi.RootBaseFragment
import com.example.course_android.di.dagger.annotations.FragmentScope
import javax.inject.Inject

@FragmentScope
class AppRouter @Inject constructor(private val fragment : RootBaseFragment)