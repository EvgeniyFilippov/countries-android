package com.example.course_android.utils

import com.example.course_android.Constants
import com.example.domain.dto.model.CountryDescriptionItemDto

fun MutableList<CountryDescriptionItemDto>.sortBySortStatusFromPref(sortStatus: Int) : MutableList<CountryDescriptionItemDto> {
    when (sortStatus) {
        Constants.SORT_STATUS_UP -> {
            this.sortBy { it.area }
        }
        Constants.SORT_STATUS_DOWN -> {
            this.sortByDescending { it.area }
        }
        else -> {
            this.sortBy {it.name}
        }
    }
    return this
}