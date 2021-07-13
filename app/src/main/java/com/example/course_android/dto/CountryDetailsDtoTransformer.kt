package com.example.course_android.dto

import com.example.course_android.Constants.DEFAULT_FLAG
import com.example.course_android.Constants.DEFAULT_LATLNG
import com.example.course_android.Constants.DEFAULT_LATLNG_SIZE
import com.example.course_android.Constants.DEFAULT_STRING
import com.example.course_android.Constants.FIRST_COUNTRY
import com.example.course_android.dto.model.CountryDescriptionItemDto
import com.example.course_android.dto.model.LanguageOfOneCountryDto
import com.example.course_android.model.allCountries.Language
import com.example.course_android.model.oneCountry.CountryDescriptionItem
import com.example.course_android.model.oneCountry.LanguageOfOneCountry

class CountryDetailsDtoTransformer :
    Transformer<MutableList<CountryDescriptionItem>, CountryDescriptionItemDto> {
    override fun transform(item: MutableList<CountryDescriptionItem>?): CountryDescriptionItemDto {
        val countryDescriptionItemDto = CountryDescriptionItemDto()
//        val defaultLanguages: List<LanguageOfOneCountryDto> = mutableListOf()

        item?.let {
            countryDescriptionItemDto.flag = it[FIRST_COUNTRY].flag ?: DEFAULT_FLAG
            countryDescriptionItemDto.name = it[FIRST_COUNTRY].name ?: DEFAULT_STRING

            countryDescriptionItemDto.latlng =
                it[FIRST_COUNTRY].latlng ?: arrayListOf(DEFAULT_LATLNG, DEFAULT_LATLNG)
            if (countryDescriptionItemDto.latlng.size != DEFAULT_LATLNG_SIZE) {
                countryDescriptionItemDto.latlng = arrayListOf(DEFAULT_LATLNG, DEFAULT_LATLNG)
            }

//            defaultLanguages[0].iso639_1 = it[FIRST_COUNTRY].languages?.get(0)?.iso639_1 ?: ""
//            defaultLanguages[0].iso639_2 = it[FIRST_COUNTRY].languages?.get(0)?.iso639_1 ?: ""
//            defaultLanguages[0].nativeName = it[FIRST_COUNTRY].languages?.get(0)?.nativeName ?: ""
//            defaultLanguages[0].name = it[FIRST_COUNTRY].languages?.get(0)?.name ?: ""
//
//            countryDescriptionItemDto.languages = defaultLanguages
        }
        return countryDescriptionItemDto
    }
}