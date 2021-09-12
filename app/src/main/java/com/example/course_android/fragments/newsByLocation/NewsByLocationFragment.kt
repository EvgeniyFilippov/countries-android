package com.example.course_android.fragments.newsByLocation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.course_android.R
import com.example.course_android.databinding.FragmentNewsBinding
import com.example.course_android.databinding.FragmentNewsByLocationBinding
import org.koin.androidx.scope.ScopeFragment

class NewsByLocationFragment : ScopeFragment(R.layout.fragment_news_by_location) {

    private var binding: FragmentNewsByLocationBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsByLocationBinding.bind(view)
    }
}