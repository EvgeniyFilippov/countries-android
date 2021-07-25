package com.example.course_android.fragments.details

import com.example.course_android.base.mvp.BaseMvpView
import com.example.course_android.dto.model.CountryDescriptionItemDto
import com.google.android.libraries.maps.model.LatLng

interface CountryDetailsView : BaseMvpView {

//    fun showCountryInfo(country: CountryDescriptionItemDto, location: LatLng)
    fun showCountryInfo(country: CountryDescriptionItemDto)

}