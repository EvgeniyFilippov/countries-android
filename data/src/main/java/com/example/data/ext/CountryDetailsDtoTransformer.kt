package com.example.data.ext

import com.example.data.NetConstants.DEFAULT_DOUBLE
import com.example.data.NetConstants.DEFAULT_FLAG
import com.example.data.NetConstants.DEFAULT_INT
import com.example.data.NetConstants.DEFAULT_LATLNG_SIZE
import com.example.data.NetConstants.DEFAULT_STRING
import com.example.data.model.oneCountry.CountryDescriptionItem
import com.example.domain.dto.model.CountryDescriptionItemDto
import com.example.domain.dto.model.LanguageOfOneCountryDto

   fun MutableList<CountryDescriptionItem>?.transformCountryToDto(): MutableList<CountryDescriptionItemDto> {

        val listCountryDescriptionItemDto: MutableList<CountryDescriptionItemDto> = mutableListOf()


        this?.forEach { country ->
            val countryDescriptionItemDto = CountryDescriptionItemDto()
            val listOfLanguagesDto: MutableList<LanguageOfOneCountryDto> = mutableListOf()

            countryDescriptionItemDto.flag = country.flag ?: DEFAULT_FLAG
            countryDescriptionItemDto.name = country.name ?: DEFAULT_STRING
            countryDescriptionItemDto.area = country.area ?: DEFAULT_DOUBLE
            countryDescriptionItemDto.capital = country.capital ?: DEFAULT_STRING
            countryDescriptionItemDto.population = country.population ?: DEFAULT_INT
            countryDescriptionItemDto.alpha2Code = country.alpha2Code ?: DEFAULT_STRING

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