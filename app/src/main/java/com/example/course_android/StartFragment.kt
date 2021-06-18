package com.example.course_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class StartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onStart() {
        super.onStart()
//        val button1: Button = this.findViewById(R.id.main_btn) ?:
//        button1.setOnClickListener {
//            (activity as MainActivity).navController.navigate(R.id.action_startFragment_to_secondFragment)
//        }
    }

}