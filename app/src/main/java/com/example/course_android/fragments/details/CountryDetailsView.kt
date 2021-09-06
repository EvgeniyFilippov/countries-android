package com.example.course_android.fragments.details

import com.example.course_android.base.mvp.BaseMvpView
import com.example.domain.dto.model.CountryDescriptionItemDto
import com.example.domain.dto.news.NewsItemDto

interface CountryDetailsView : BaseMvpView {

    fun showCountryInfo(countryList: List<CountryDescriptionItemDto>)

    fun showNews(news: MutableList<NewsItemDto>)

}