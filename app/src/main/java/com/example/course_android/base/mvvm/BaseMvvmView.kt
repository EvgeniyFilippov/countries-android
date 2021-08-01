package com.example.course_android.base.mvvm

interface BaseMvvmView {

    fun showError(error: String)

    fun showProgress()

    fun hideProgress()
}