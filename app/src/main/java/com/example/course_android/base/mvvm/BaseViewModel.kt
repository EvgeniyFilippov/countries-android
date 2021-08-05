package com.example.course_android.base.mvvm

import androidx.annotation.CallSuper
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

open class BaseViewModel(protected val savedStateHandle: SavedStateHandle) : ViewModel() {

    //val vm: BaseViewModel by viewModels()
    //val mCountryLiveData = MutableLiveData<Outcome<PostCountryItemDto>>()
    protected val mCompositeDisposable: CompositeDisposable = CompositeDisposable()


    @CallSuper
    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.clear()
    }

}