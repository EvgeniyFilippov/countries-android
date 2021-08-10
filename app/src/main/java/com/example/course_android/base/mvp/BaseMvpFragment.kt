package com.example.course_android.base.mvp

import android.os.Bundle
import androidx.fragment.app.Fragment
import org.koin.androidx.scope.ScopeFragment
import org.koin.core.scope.Scope

abstract class BaseMvpFragment<View: BaseMvpView, PresenterType : BaseMvpPresenter<View>> : ScopeFragment() {

    protected lateinit var mPresenter: PresenterType

    abstract  fun createPresenter()

    abstract fun getPresenter() : PresenterType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createPresenter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        getPresenter().onDestroyView()
        getPresenter().detachView()
    }
}