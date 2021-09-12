package com.example.course_android.base.mvi

import androidx.annotation.LayoutRes
import org.koin.androidx.scope.ScopeFragment

abstract class BaseMviFragment<INTENT : ViewIntent, ACTION : ViewAction, STATE : ViewState>:
    IViewRenderer<STATE>,
    ScopeFragment() {
    private lateinit var viewState: STATE
    val mState get() = viewState

//    private val viewModel: VM by lazy {
//        viewModelProvider(
//            this.viewModelFactory,
//            modelClass.kotlin
//        )
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(getLayoutResId())
//        initUI()
//        viewModel.state.observe(this, {
//            viewState = it
//            render(it)
//        })
//        initDATA()
//        initEVENT()
//    }


    @LayoutRes
    abstract fun getLayoutResId(): Int
    abstract fun initUI()
    abstract fun initDATA()
    abstract fun initEVENT()
//    fun dispatchIntent(intent: INTENT) {
//        viewModel.dispatchIntent(intent)
//    }
}