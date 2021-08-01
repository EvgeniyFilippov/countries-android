package com.example.course_android.base.mvp

interface BaseMvpView {

    fun showError(error: String, throwable: Throwable)

    fun showProgress()

    fun hideProgress()
}