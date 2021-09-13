package com.example.course_android.fragments.newsByLocation

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.course_android.R
import com.example.course_android.adapters.AdapterNews
import com.example.course_android.base.mvi.BaseMviFragment
import com.example.course_android.databinding.FragmentNewsByLocationBinding
import com.example.course_android.ext.getMessage
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class NewsByLocationFragment :
    BaseMviFragment<NewsIntent, NewsAction, NewsState>() {

    private var binding: FragmentNewsByLocationBinding? = null
    private val viewModel: NewsByLocationViewModel by stateViewModel()
    var adapterNews = AdapterNews()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsByLocationBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        viewModel.state.observe(viewLifecycleOwner, {
            viewState = it
            render(it)
        })
        initDATA()
        initEVENT()
    }

    override fun initUI() {
        binding?.recyclerLocalNews?.setHasFixedSize(true)
        binding?.recyclerLocalNews?.layoutManager = LinearLayoutManager(context)
        binding?.recyclerLocalNews?.adapter = adapterNews
    }

    override fun initDATA() {
        dispatchIntent(NewsIntent.LoadAllCharacters)
    }

    override fun initEVENT() {

    }

    override fun render(state: NewsState) {
//        binding?.progressLocalNews?.isVisible = state is NewsState.Loading
        when (state) {
            is NewsState.ResultAllPersona -> {
                if (state.data.isNotEmpty()) {
                    adapterNews.repopulate(state.data.toMutableList())
                } else {
                    binding?.newsMessage?.text = getString(R.string.noLocalNews)
                }
            }

            is NewsState.Loading -> {
                if (state.loading) {
                    binding?.progressLocalNews?.visibility = View.VISIBLE
                } else {
                    binding?.progressLocalNews?.visibility = View.GONE
                }

            }

            is NewsState.Exception -> {
                binding?.newsMessage?.text = context?.let { state.callErrors.message }
            }
        }
    }

    override fun dispatchIntent(intent: NewsIntent) {
        viewModel.dispatchIntent(intent)
    }
}