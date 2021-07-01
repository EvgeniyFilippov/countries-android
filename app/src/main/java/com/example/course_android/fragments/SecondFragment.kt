package com.example.course_android.fragments

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.course_android.api.CountriesApi
import com.example.course_android.MyAdapter
import com.example.course_android.R
import com.example.course_android.databinding.FragmentSecondBinding
import com.example.course_android.model.CountriesDataItem
import kotlinx.android.synthetic.main.fragment_second.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SecondFragment : Fragment(R.layout.fragment_second) {

    lateinit var myAdapter: MyAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var responseBody: MutableList<CountriesDataItem>
    private var binding: FragmentSecondBinding? = null
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://restcountries.eu/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSecondBinding.bind(view)
        recyclerView.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        setHasOptionsMenu(true)
        getMyData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.countries_sort_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sort_up) {
            responseBody.sortBy { it.population }
            myAdapter.notifyDataSetChanged()

        } else if (item.itemId == R.id.sort_down) {
            responseBody.sortByDescending { it.population }
            myAdapter.notifyDataSetChanged()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getMyData() {
        val countriesApi = retrofit.create(CountriesApi::class.java)
        val countriesApiCall = countriesApi.getTopHeadlines()

        countriesApiCall.enqueue(object : Callback<List<CountriesDataItem>?> {
            override fun onResponse(
                call: Call<List<CountriesDataItem>?>,
                response: Response<List<CountriesDataItem>?>
            ) {
                if (response.body() != null) {
                    responseBody = (response.body() as MutableList<CountriesDataItem>)
                    myAdapter = MyAdapter(this, responseBody)
                    recyclerView.adapter = myAdapter
                } else {
                    Log.d("RETROFIT_COUNTRIES", response.body().toString())
                }
            }

            override fun onFailure(call: Call<List<CountriesDataItem>?>, t: Throwable) {
                Log.d("RETROFIT_COUNTRIES", t.toString())
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}