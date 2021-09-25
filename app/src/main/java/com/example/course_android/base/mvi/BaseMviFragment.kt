package com.example.course_android.base.mvi

import org.koin.androidx.scope.ScopeFragment

abstract class BaseMviFragment<INTENT : ViewIntent, ACTION : ViewAction, STATE : ViewState> :
    IViewRenderer<STATE>,
    ScopeFragment() {
    lateinit var viewState: STATE
    val mState get() = viewState

    abstract fun initUI()
    abstract fun initDATA()
    abstract fun dispatchIntent(intent: INTENT)
}