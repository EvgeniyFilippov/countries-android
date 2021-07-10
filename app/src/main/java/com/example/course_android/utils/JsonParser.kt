package com.example.course_android.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder


class JsonParser {
    private var gson: Gson? = null

    private fun getGsonParser(): Gson? {
        if (null == gson) {
            val builder = GsonBuilder()
            gson = builder.create()
        }
        return gson
    }
}