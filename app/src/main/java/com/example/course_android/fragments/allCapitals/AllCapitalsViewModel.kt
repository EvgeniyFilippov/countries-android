package com.example.course_android.fragments.allCapitals

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.course_android.Constants.ALL_CAPITALS_LIVE_DATA
import com.example.course_android.base.mvvm.BaseViewModel
import com.example.course_android.base.mvvm.Outcome
import com.example.domain.dto.model.CapitalItemDto
import com.example.domain.usecase.impl.GetCapitalsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllCapitalsViewModel(
    savedStateHandle: SavedStateHandle,
    private val mGetCapitalUseCase: GetCapitalsUseCase
) : BaseViewModel(savedStateHandle) {

    val allCapitalsLiveData =
        savedStateHandle.getLiveData<Outcome<MutableList<CapitalItemDto>>>(
            ALL_CAPITALS_LIVE_DATA
        )

    fun getCapitalsCoroutines() {
        CoroutineScope(viewModelScope.coroutineContext).launch {
            try {
                allCapitalsLiveData.value = Outcome.loading(true)
                val result = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
                    mGetCapitalUseCase.execute()
                }
                allCapitalsLiveData.value = Outcome.loading(false)
                allCapitalsLiveData.value = Outcome.success(result)
            } catch (e: Exception) {
                allCapitalsLiveData.value = Outcome.loading(false)
                allCapitalsLiveData.value = Outcome.failure(e)
            }
        }
    }
}