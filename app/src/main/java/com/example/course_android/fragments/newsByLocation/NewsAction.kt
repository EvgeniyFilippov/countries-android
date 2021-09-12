package com.example.course_android.fragments.newsByLocation

import com.example.course_android.base.mvi.ViewAction

sealed class NewsAction : ViewAction {

    object AllCharacters : NewsAction()
}