package com.example.course_android.fragments.newsByLocation

import com.example.course_android.base.mvi.ViewIntent

sealed class NewsIntent : ViewIntent {
    object LoadAllCharacters : NewsIntent()
    data class SearchCharacter(val name: String) : NewsIntent()
    object ClearSearch : NewsIntent()
}