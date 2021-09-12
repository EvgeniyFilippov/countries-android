package com.example.course_android.fragments.newsByLocation

import com.example.course_android.base.mvi.ViewAction

sealed class NewsAction : ViewAction {
    data class SearchCharacters(val name: String) : NewsAction()
    object AllCharacters : NewsAction()
}