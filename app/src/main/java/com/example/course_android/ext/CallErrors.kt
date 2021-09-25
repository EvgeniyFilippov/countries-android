package com.example.course_android.ext

import android.content.Context
import com.example.course_android.R
import com.example.course_android.base.mvi.CallErrors

fun CallErrors.getMessage(context: Context): String {
    return when (this) {
        is CallErrors.ErrorEmptyData -> context.getString(R.string.error_empty_data)
        is CallErrors.ErrorServer -> context.getString(R.string.error_server_error)
        is CallErrors.ErrorException ->  context.getString(
            R.string.error_exception
        )
    }

}