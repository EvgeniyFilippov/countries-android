package com.example.course_android.fragments.details

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.course_android.CountriesApp
import com.example.course_android.R
import com.example.course_android.api.CountryDescriptionApi
import com.example.course_android.api.RetrofitObj
import com.example.course_android.base.mvp.BaseMvpPresenter
import com.example.course_android.dto.CountryDetailsDtoTransformer
import com.example.course_android.dto.model.LanguageOfOneCountryDto
import com.example.course_android.model.oneCountry.CountryDescriptionItem
import com.example.course_android.utils.loadSvg
import com.google.android.libraries.maps.SupportMapFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_country_details.*


class CountryDetailsPresenter : BaseMvpPresenter<CountryDetailsView>() {
    private val countryDetailsDtoTransformer = CountryDetailsDtoTransformer()

    fun getMyData(mCountryName: String, isRefresh: Boolean) {
        RetrofitObj.getOkHttp()
        val countryDescrApi = CountriesApp.retrofit.create(CountryDescriptionApi::class.java)
        addDisposable(
            inBackground(
                handleProgress(countryDescrApi.getTopHeadlines(mCountryName), isRefresh)
            ).subscribe({ response ->

                getView()?.showCountryInfo(
                    countryDetailsDtoTransformer.transform(response))

            }, {
                it.message?.let { it1 -> getView()?.showError(it1, it) }
            })
        )
    }
}