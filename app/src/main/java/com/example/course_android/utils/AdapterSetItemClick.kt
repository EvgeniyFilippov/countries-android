package com.example.course_android.utils

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController

import com.example.course_android.Constants
import com.example.course_android.MyAdapter
import com.example.course_android.R
import com.google.gson.GsonBuilder

fun mySetItemClick(adapter: MyAdapter) {
//    adapter.setItemClick { item ->
//        val bundle = Bundle()
//        val gson = GsonBuilder().create()
//        bundle.putString(Constants.COUNTRY_NAME_KEY, item.name)
//        bundle.putString(Constants.LANGUAGES_LIST_KEY, gson.toJson(item.languages))
//        bundle.putString(Constants.COUNTRY_FLAG_KEY, item.flag)
//        findNavController().navigate(
//            R.id.action_secondFragment_to_countryDetailsFragment,
//            bundle
//        )
//    }
}