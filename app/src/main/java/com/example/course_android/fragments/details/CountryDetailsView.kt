package com.example.course_android.fragments.details

import com.example.course_android.base.mvp.BaseMvpView
import com.example.domain.dto.model.CountryDescriptionItemDto

interface CountryDetailsView : BaseMvpView {

    fun showCountryInfo(country: List<CountryDescriptionItemDto>)

}