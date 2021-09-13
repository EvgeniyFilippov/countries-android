package com.example.course_android.fragments.newsByLocation

import com.example.course_android.base.mvi.ViewIntent

sealed class NewsIntent : ViewIntent {
    object LoadNewsIntent : NewsIntent()
}