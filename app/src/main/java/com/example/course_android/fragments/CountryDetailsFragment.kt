package com.example.course_android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.course_android.Constants
import com.example.course_android.databinding.FragmentCountryDetailsBinding
import com.example.course_android.model.Language
import com.google.gson.Gson
import com.google.gson.GsonBuilder


class CountryDetailsFragment : Fragment() {

    private lateinit var mCountryName: String
    private lateinit var mLanguageJsonString: String
    private lateinit var binding: FragmentCountryDetailsBinding
    private var mLanguageList: List<Language>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCountryName = arguments?.getString(Constants.COUNTRY_NAME_KEY) ?: Constants.ERROR
        mLanguageJsonString = arguments?.getString(Constants.LANGUAGES_LIST) ?: Constants.ERROR

        val gson = GsonBuilder().create()
        mLanguageList = gson.fromJson(mLanguageJsonString, Array<Language>::class.java)?.toList()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCountryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mTvCountryName.text = mCountryName
        binding.mTvLanguageJsonString.text = mLanguageList.toString()
    }

    companion object {
        private var gson: Gson? = null

        private fun getGsonParser(): Gson? {
            if (null == gson) {
                val builder = GsonBuilder()
                gson = builder.create()
            }
            return gson
        }
    }
}