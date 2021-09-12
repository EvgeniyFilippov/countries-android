package com.example.course_android.fragments.newsByLocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.course_android.adapters.AdapterNews
import com.example.course_android.base.mvi.BaseMviFragment
import com.example.course_android.databinding.FragmentNewsByLocationBinding
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

    }

    override fun initEVENT() {

    }

    override fun render(state: NewsState) {

    }
}