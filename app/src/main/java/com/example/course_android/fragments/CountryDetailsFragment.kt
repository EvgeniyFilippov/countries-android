package com.example.course_android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.course_android.Constants
import com.example.course_android.R
import com.example.course_android.databinding.FragmentCountryDetailsBinding

class CountryDetailsFragment : Fragment() {

    private lateinit var mCountryName: String
    private lateinit var binding: FragmentCountryDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCountryName = arguments?.getString(Constants.COUNTRY_NAME_KEY) ?: Constants.ERROR
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
    }
}