package com.example.course_android.utils

import androidx.lifecycle.MutableLiveData
import com.example.course_android.Constants
import com.example.course_android.dto.model.CountryDescriptionItemDto

fun MutableLiveData<MutableList<CountryDescriptionItemDto>>.sortBySortStatusFromPref(sortStatus: Int) {
    when (sortStatus) {
        Constants.SORT_STATUS_UP -> {
            this.value?.sortBy { it.area }
        }
        Constants.SORT_STATUS_DOWN -> {
            this.value?.sortByDescending { it.area }
        }
        else -> {
            this.value?.sortBy {it.name}
        }
    }
}