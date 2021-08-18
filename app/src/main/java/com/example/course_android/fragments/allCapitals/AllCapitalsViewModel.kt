package com.example.course_android.fragments.allCapitals

import androidx.lifecycle.SavedStateHandle
import com.example.course_android.Constants.ALL_CAPITALS_LIVE_DATA
import com.example.course_android.base.mvvm.*
import com.example.domain.dto.model.CapitalItemDto
import com.example.domain.usecase.impl.GetCapitalsUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AllCapitalsViewModel(
    savedStateHandle: SavedStateHandle,
    private val mGetCapitalUseCase: GetCapitalsUseCase,

) : BaseViewModel(savedStateHandle) {

    val allCapitalsLiveData =
        savedStateHandle.getLiveData<Outcome<MutableList<CapitalItemDto>>>(
            ALL_CAPITALS_LIVE_DATA
        )

    fun getCapitalsFromApi() {
        mGetCapitalUseCase.execute()
            .map { it }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                allCapitalsLiveData.next(it)
            }, {
                allCapitalsLiveData.failed(it)
            }, {
                if (allCapitalsLiveData.value is Outcome.Next) {
                    allCapitalsLiveData.success((allCapitalsLiveData.value as Outcome.Next).data)
                }
            }).also { mCompositeDisposable.add(it) }
    }

}