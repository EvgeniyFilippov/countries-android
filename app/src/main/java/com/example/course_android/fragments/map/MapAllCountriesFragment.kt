package com.example.course_android.fragments.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.course_android.R
import com.example.course_android.base.mvp.BaseMvpFragment
import com.example.course_android.databinding.FragmentCountryDetailsBinding
import com.example.course_android.databinding.FragmentMapAllCountriesBinding
import com.example.course_android.fragments.details.CountryDetailsPresenter

class MapAllCountriesFragment : BaseMvpFragment<MapAllCountriesView, MapAllCountriesPresenter>(), MapAllCountriesView {

    private var binding: FragmentMapAllCountriesBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapAllCountriesBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_map_all_countries, container, false)
    }

    override fun createPresenter() {
        mPresenter = MapAllCountriesPresenter()
    }

    override fun getPresenter(): MapAllCountriesPresenter = mPresenter

    override fun showAllCountriesOnMap() {
        TODO("Not yet implemented")
    }

    override fun showError(error: String, throwable: Throwable) {
        TODO("Not yet implemented")
    }

    override fun showProgress() {
        binding?.progressMap?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding?.progressMap?.visibility = View.GONE
    }


}