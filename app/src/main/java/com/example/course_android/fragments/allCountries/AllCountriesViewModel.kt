package com.example.course_android.fragments.allCountries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.course_android.CountriesApp
import com.example.course_android.R
import com.example.course_android.api.RetrofitObj
import com.example.course_android.base.mvvm.BaseViewModel
import com.example.course_android.dto.CountryDetailsDtoTransformer
import com.example.course_android.dto.model.CountryDescriptionItemDto
import com.example.course_android.utils.convertDBdataToRetrofitModel
import com.example.course_android.utils.sortBySortStatusFromPref
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AllCountriesViewModel(private val sortStatus: Int) : BaseViewModel() {

    private val mutableCountriesLiveData = MutableLiveData<MutableList<CountryDescriptionItemDto>>()
    val countriesLiveData: LiveData<MutableList<CountryDescriptionItemDto>> =
        mutableCountriesLiveData

    private val mutableCountriesErrorLiveData = MutableLiveData<String>()
    val countriesErrorLiveData: LiveData<String> = mutableCountriesErrorLiveData

    private var listOfCountriesFromDB: MutableList<CountryDescriptionItemDto> = arrayListOf()
    private val countryDetailsDtoTransformer = CountryDetailsDtoTransformer()

    fun getCountriesFromApi() {
//        val progressBar = binding?.progressBar as ProgressBar
//        progressBar.visibility = ProgressBar.VISIBLE
        RetrofitObj.getCountriesApi().getListOfCountry()
            .map { list -> countryDetailsDtoTransformer.transform(list) }
            .doOnNext { listDto -> listDto.sortBySortStatusFromPref(sortStatus)}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ sortedListDto ->
                mutableCountriesLiveData.value = sortedListDto
//                saveToDBfromApi()
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
            ?.map { list -> list.convertDBdataToRetrofitModel(languagesFromDB,
                listOfCountriesFromDB) }
            ?.doOnNext { list -> list.sortBySortStatusFromPref(sortStatus) }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ sortedListDto ->
                mutableCountriesLiveData.value = sortedListDto
                sortedListDto.clear()
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