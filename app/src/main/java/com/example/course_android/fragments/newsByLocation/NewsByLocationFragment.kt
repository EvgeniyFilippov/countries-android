package com.example.course_android.fragments.newsByLocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.course_android.base.mvi.BaseMviFragment
import com.example.course_android.databinding.FragmentNewsByLocationBinding
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class NewsByLocationFragment :
    BaseMviFragment<NewsIntent, NewsAction, NewsState>() {

    private var binding: FragmentNewsByLocationBinding? = null
    private val viewModel: NewsByLocationViewModel by stateViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsByLocationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun getLayoutResId(): Int {
        TODO("Not yet implemented")
    }

    override fun initUI() {
        TODO("Not yet implemented")
    }

    override fun initDATA() {
        TODO("Not yet implemented")
    }

    override fun initEVENT() {
        TODO("Not yet implemented")
    }

    override fun render(state: NewsState) {
        TODO("Not yet implemented")
    }
}