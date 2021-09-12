package com.example.course_android.base.mvi

sealed class CallErrors {
    object ErrorEmptyData : CallErrors()
    object ErrorServer: CallErrors()
    data class ErrorException(val throwable: Throwable) : CallErrors()
}