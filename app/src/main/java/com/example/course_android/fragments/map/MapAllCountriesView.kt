package com.example.course_android.fragments.map

import com.example.course_android.base.mvp.BaseMvpView
import com.example.course_android.model.allCountries.CountriesDataItem

interface MapAllCountriesView : BaseMvpView {

    fun showAllCountriesOnMap(listOfCountries: List<CountriesDataItem>)
}