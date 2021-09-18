package com.example.course_android.base.mvi

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.course_android.CountriesApp
import com.example.course_android.base.mvvm.BaseViewModel
import com.example.course_android.di.dagger.component.FragmentComponent
import com.example.course_android.di.dagger.module.FragmentModule
import com.example.course_android.di.dagger.viewmodels.DaggerViewModelFactory
import org.koin.androidx.scope.ScopeFragment
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseMviFragment<
        INTENT : ViewIntent,
        ACTION : ViewAction,
        STATE : ViewState,
        VM : BaseMviViewModel<INTENT, ACTION, STATE>>(private val modelClass: Class<VM>) :
    RootBaseFragment(), IViewRenderer<STATE> {
    lateinit var viewState: STATE
    val mState get() = viewState

    private val viewModel: VM by lazy {
        viewModelProvider(
            this.viewModelFactory,
            modelClass.kotlin
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        viewModel.state.observe(viewLifecycleOwner, {
            viewState = it
            render(it)
        })
        initDATA()
    }

    abstract fun initUI()
    abstract fun initDATA()
    fun dispatchIntent(intent: INTENT) {
        viewModel.dispatchIntent(intent)
    }

}