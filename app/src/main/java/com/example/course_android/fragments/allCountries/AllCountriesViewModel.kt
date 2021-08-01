package com.example.course_android.fragments.allCountries

import android.os.Bundle
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.course_android.Constants
import com.example.course_android.CountriesApp
import com.example.course_android.R
import com.example.course_android.api.RetrofitObj
import com.example.course_android.base.mvvm.BaseViewModel
import com.example.course_android.dto.CountryDetailsDtoTransformer
import com.example.course_android.dto.model.CountryDescriptionItemDto
import com.example.course_android.ext.isOnline
import com.example.course_android.model.oneCountry.CountryDescriptionItem
import com.example.course_android.utils.convertDBdataToRetrofitModel
import com.example.course_android.utils.sortBySortStatusFromPref
import com.example.course_android.utils.toast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AllCountriesViewModel(
    private var sortStatus: Int
) : BaseViewModel() {

    private val mutableCountriesLiveData = MutableLiveData<MutableList<CountryDescriptionItemDto>>()
    val countriesLiveData: LiveData<MutableList<CountryDescriptionItemDto>> =
        mutableCountriesLiveData

    private val mutableCountriesErrorLiveData = MutableLiveData<String>()
    val countriesErrorLiveData: LiveData<String> = mutableCountriesErrorLiveData


    private val countryDetailsDtoTransformer = CountryDetailsDtoTransformer()

    fun getCountriesFromApi() {
//        val progressBar = binding?.progressBar as ProgressBar
//        progressBar.visibility = ProgressBar.VISIBLE
        RetrofitObj.getCountriesApi().getListOfCountry()
            .map { list -> countryDetailsDtoTransformer.transform(list) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ listDto ->
                mutableCountriesLiveData.value = listDto
                mutableCountriesLiveData.sortBySortStatusFromPref(sortStatus)
                saveToDBfromApi()
            }, { getCountriesFromDB()
//                if (cone?.isOnline() == false) {
//                    mutableCountriesErrorLiveData.value = "Error"
//                    context?.toast(getString(R.string.chek_inet))
//                }
//                progressBar.visibility = ProgressBar.GONE
            }).also { mCompositeDisposable.add(it) }
    }

    private fun getCountriesFromDB() {
        val countriesFromDB = CountriesApp.base?.getCountryInfoDAO()?.getAllInfo()
        val languagesFromDB = CountriesApp.base?.getLanguageInfoDAO()
        countriesFromDB
            ?.doOnNext { list ->
                listOfCountriesFromDB = list.convertDBdataToRetrofitModel(
                    languagesFromDB,
                    listOfCountriesFromDB
                )
                listOfCountriesFromDB.sortBySortStatusFromPref(sortStatus)
            }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                adapterOfAllCountries.repopulate(
                    listOfCountriesFromDB
                )
                listOfCountriesFromDB.clear()
                adapterOfAllCountries.setItemClick {
                    if (context?.isOnline() == false) {
                        context?.toast(getString(R.string.chek_inet))
                    } else {
                        getCountriesFromApi()
                    }
                }
            }, { throwable ->
                throwable.printStackTrace()
            }).also { mCompositeDisposable.add(it) }
    }

}