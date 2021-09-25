package com.example.course_android.base.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

fun <T : ViewModel> RootBaseFragment.viewModelProvider(
    factory: ViewModelProvider.Factory,
    model: KClass<T>
): T {
    return ViewModelProvider(this, factory).get(model.java)
}
