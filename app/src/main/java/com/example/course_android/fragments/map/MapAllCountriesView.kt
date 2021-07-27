package com.example.course_android.fragments.map

import com.example.course_android.base.mvp.BaseMvpView
import com.example.course_android.dto.model.CountryDescriptionItemDto

interface MapAllCountriesView : BaseMvpView {

    fun showAllCountriesOnMap(listOfCountries: List<CountryDescriptionItemDto>)
}