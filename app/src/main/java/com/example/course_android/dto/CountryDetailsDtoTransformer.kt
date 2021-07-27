package com.example.course_android.dto

import com.example.course_android.Constants.DEFAULT_DOUBLE
import com.example.course_android.Constants.DEFAULT_FLAG
import com.example.course_android.Constants.DEFAULT_LATLNG_SIZE
import com.example.course_android.Constants.DEFAULT_STRING
import com.example.course_android.dto.model.CountryDescriptionItemDto
import com.example.course_android.dto.model.LanguageOfOneCountryDto
import com.example.course_android.model.oneCountry.CountryDescriptionItem

class CountryDetailsDtoTransformer :
    Transformer<List<CountryDescriptionItem>, List<CountryDescriptionItemDto>> {

    override fun transform(item: List<CountryDescriptionItem>?): MutableList<CountryDescriptionItemDto> {

        val listCountryDescriptionItemDto: MutableList<CountryDescriptionItemDto> = mutableListOf()


        item?.forEach { country ->
            val countryDescriptionItemDto = CountryDescriptionItemDto()
            val listOfLanguagesDto: MutableList<LanguageOfOneCountryDto> = mutableListOf()

            countryDescriptionItemDto.flag = country.flag ?: DEFAULT_FLAG
            countryDescriptionItemDto.name = country.name ?: DEFAULT_STRING
            countryDescriptionItemDto.area = country.area ?: DEFAULT_DOUBLE
            countryDescriptionItemDto.capital = country.capital ?: DEFAULT_STRING

            countryDescriptionItemDto.latlng =
                country.latlng ?: arrayListOf(DEFAULT_DOUBLE, DEFAULT_DOUBLE)
            if (countryDescriptionItemDto.latlng.size != DEFAULT_LATLNG_SIZE) {
                countryDescriptionItemDto.latlng = arrayListOf(DEFAULT_DOUBLE, DEFAULT_DOUBLE)
            }

            var count = 0;
            country.languages?.forEach {
                val languageDto = LanguageOfOneCountryDto()
                languageDto.iso639_1 = it.iso639_1 ?: ""
                languageDto.iso639_2 = it.iso639_2 ?: ""
                languageDto.name = it.name ?: ""
                languageDto.nativeName = it.nativeName ?: ""
                listOfLanguagesDto.add(languageDto)
                count++
            }
            countryDescriptionItemDto.languages = listOfLanguagesDto
            listCountryDescriptionItemDto.add(countryDescriptionItemDto)
        }

        return listCountryDescriptionItemDto

    }


}