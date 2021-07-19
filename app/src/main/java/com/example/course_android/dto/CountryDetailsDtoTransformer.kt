package com.example.course_android.dto

import com.example.course_android.Constants.DEFAULT_FLAG
import com.example.course_android.Constants.DEFAULT_LATLNG
import com.example.course_android.Constants.DEFAULT_LATLNG_SIZE
import com.example.course_android.Constants.DEFAULT_STRING
import com.example.course_android.Constants.FIRST_COUNTRY
import com.example.course_android.dto.model.CountryDescriptionItemDto
import com.example.course_android.dto.model.LanguageOfOneCountryDto
import com.example.course_android.model.oneCountry.CountryDescriptionItem

class CountryDetailsDtoTransformer :
    Transformer<MutableList<CountryDescriptionItem>, CountryDescriptionItemDto> {
    override fun transform(item: MutableList<CountryDescriptionItem>?): CountryDescriptionItemDto {
        val countryDescriptionItemDto = CountryDescriptionItemDto()
        val listOfLanguagesDto: MutableList<LanguageOfOneCountryDto> = mutableListOf()

        item?.let {
            countryDescriptionItemDto.flag = it[FIRST_COUNTRY].flag ?: DEFAULT_FLAG
            countryDescriptionItemDto.name = it[FIRST_COUNTRY].name ?: DEFAULT_STRING

            countryDescriptionItemDto.latlng =
                it[FIRST_COUNTRY].latlng ?: arrayListOf(DEFAULT_LATLNG, DEFAULT_LATLNG)
            if (countryDescriptionItemDto.latlng.size != DEFAULT_LATLNG_SIZE) {
                countryDescriptionItemDto.latlng = arrayListOf(DEFAULT_LATLNG, DEFAULT_LATLNG)
            }

            var count = 0;
            it[FIRST_COUNTRY].languages?.forEach {
                val languageDto = LanguageOfOneCountryDto()
                languageDto.iso639_1 = it.iso639_1 ?: ""
                languageDto.iso639_2 = it.iso639_2 ?: ""
                languageDto.name = it.name ?: ""
                languageDto.nativeName = it.nativeName ?: ""
                listOfLanguagesDto.add(languageDto)
                count++
            }
            countryDescriptionItemDto.languages = listOfLanguagesDto
        }
        return countryDescriptionItemDto
    }
}