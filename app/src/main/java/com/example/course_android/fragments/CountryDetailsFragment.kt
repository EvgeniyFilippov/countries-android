package com.example.course_android.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.course_android.AdapterLanguages
import com.example.course_android.Constants
import com.example.course_android.databinding.FragmentCountryDetailsBinding
import com.example.course_android.model.Language
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_country_details.*
import kotlinx.android.synthetic.main.fragment_second.*


class CountryDetailsFragment : Fragment() {

    private lateinit var mCountryName: String
    private lateinit var mCountryFlagString: String
    private lateinit var mLanguageJsonString: String
    private lateinit var binding: FragmentCountryDetailsBinding
    private var mLanguageList: List<Language>? = null
    lateinit var adapterLanguages: AdapterLanguages
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCountryName = arguments?.getString(Constants.COUNTRY_NAME_KEY) ?: Constants.ERROR

        mLanguageJsonString = arguments?.getString(Constants.LANGUAGES_LIST_KEY) ?: Constants.ERROR
        val gson = GsonBuilder().create()
        mLanguageList = gson.fromJson(mLanguageJsonString, Array<Language>::class.java)?.toList()

        mCountryFlagString = arguments?.getString(Constants.COUNTRY_FLAG_KEY) ?: Constants.ERROR

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
        val uri = Uri.parse(mCountryFlagString)
        GlideToVectorYou.justLoadImage(activity, uri, item_flag)

        linearLayoutManager = LinearLayoutManager(context)
        recycler_languages.layoutManager = linearLayoutManager
        adapterLanguages = AdapterLanguages()

        val params: ViewGroup.LayoutParams = recycler_languages.layoutParams
        params.height = mLanguageList?.size?.times(Constants.LANGUAGE_VIEW_HEIGHT) ?: Constants.DEFAULT_INT
        recycler_languages.layoutParams = params

        recycler_languages.adapter = adapterLanguages
        adapterLanguages.repopulate(mLanguageList as MutableList<Language>)
    }
}