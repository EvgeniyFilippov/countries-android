package com.example.course_android.dto

import com.example.course_android.Constants.DEFAULT_INT
import com.example.course_android.Constants.END_AREA_FILTER_KEY
import com.example.course_android.Constants.END_DISTANCE_FILTER_KEY
import com.example.course_android.Constants.START_AREA_FILTER_KEY
import com.example.course_android.Constants.START_DISTANCE_FILTER_KEY

class FilterSettingsToDtoTransformer :
    Transformer<HashMap<String?, Int>, HashMap<String, Int>> {

    override fun transform(item: HashMap<String?, Int>?): HashMap<String, Int> {

        val mapSettingsByFilterDto: HashMap<String, Int> = hashMapOf()

        mapSettingsByFilterDto[START_AREA_FILTER_KEY] =
            item?.get(START_AREA_FILTER_KEY) ?: DEFAULT_INT

        mapSettingsByFilterDto[END_AREA_FILTER_KEY] =
            item?.get(END_AREA_FILTER_KEY) ?: DEFAULT_INT

        mapSettingsByFilterDto[START_DISTANCE_FILTER_KEY] =
        item?.get(START_DISTANCE_FILTER_KEY) ?: DEFAULT_INT
        mapSettingsByFilterDto[END_DISTANCE_FILTER_KEY] =
        item?.get(END_DISTANCE_FILTER_KEY) ?: DEFAULT_INT

        return mapSettingsByFilterDto
    }

}