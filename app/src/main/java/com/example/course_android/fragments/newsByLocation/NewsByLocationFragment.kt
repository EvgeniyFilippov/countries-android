package com.example.course_android.fragments.newsByLocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.course_android.R
import com.example.course_android.adapters.AdapterNews
import com.example.course_android.base.mvi.BaseMviFragment
import com.example.course_android.base.mvvm.BaseMvvmView
import com.example.course_android.databinding.FragmentNewsByLocationBinding
import com.example.course_android.ext.askLocationPermission
import com.example.course_android.ext.checkLocationPermission
import com.example.course_android.ext.isOnline
import com.example.course_android.ext.showAlertDialog
import com.example.course_android.utils.toast
import org.koin.androidx.viewmodel.ext.android.stateViewModel

private const val LOCATION_PERMISSION_CODE = 1000

class NewsByLocationFragment :
    BaseMviFragment<NewsIntent, NewsAction, NewsState, NewsByLocationViewModel>(NewsByLocationViewModel::class.java),
    BaseMvvmView {

    private var binding: FragmentNewsByLocationBinding? = null
    var adapterNews = AdapterNews()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsByLocationBinding.inflate(inflater, container, false)
        if (context?.checkLocationPermission() == false) {
            activity?.askLocationPermission(LOCATION_PERMISSION_CODE)
        }
        return binding?.root
    }


    override fun initUI() {
        binding?.recyclerLocalNews?.setHasFixedSize(true)
        binding?.recyclerLocalNews?.layoutManager = LinearLayoutManager(context)
        binding?.recyclerLocalNews?.adapter = adapterNews
    }

    override fun initDATA() {
        dispatchIntent(NewsIntent.LoadNewsIntent)
    }

    override fun render(state: NewsState) {

        when (state) {
            is NewsState.ResultAllNews -> {
                if (state.data.isNotEmpty()) {
                    adapterNews.repopulate(state.data.toMutableList())
                } else {
                    binding?.newsMessage?.text = getString(R.string.noLocalNews)
                }
            }

            is NewsState.Loading -> {
                if (state.loading) showProgress() else hideProgress()
            }

            is NewsState.Exception -> {
                binding?.newsMessage?.text = context?.let { state.callErrors.message }
            }
        }
    }

    override fun showError() {
        hideProgress()
        if (context?.isOnline() == false) {
            context?.toast(getString(R.string.chek_inet))
        } else {
            activity?.showAlertDialog()
        }
    }

    override fun showProgress() {
        binding?.progressLocalNews?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding?.progressLocalNews?.visibility = View.GONE
    }

}