package com.example.course_android.fragments

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.course_android.MyAdapter
import com.example.course_android.R
import com.example.course_android.api.CountriesApi
import com.example.course_android.databinding.FragmentSecondBinding
import com.example.course_android.model.CountriesDataItem
import kotlinx.android.synthetic.main.fragment_second.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SecondFragment : Fragment(R.layout.fragment_second) {

    lateinit var myAdapter: MyAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var responseBody: MutableList<CountriesDataItem>
    private var binding: FragmentSecondBinding? = null
    private val okHttpClientBuilder = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
    private val logging = HttpLoggingInterceptor()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://restcountries.eu/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientBuilder.build())
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
        if (item.itemId == R.id.sort_countries) {
            if (!item.isChecked) {
                responseBody.sortBy { it.population }
                item.setIcon(R.drawable.ic_baseline_keyboard_arrow_down_24)
                Toast.makeText(context,getString(R.string.sort_up),
                    Toast.LENGTH_SHORT).show();
                item.isChecked = true
            } else {
                responseBody.sortByDescending { it.population }
                item.setIcon(R.drawable.ic_baseline_keyboard_arrow_up_24)
                Toast.makeText(context,getString(R.string.sort_down),
                    Toast.LENGTH_SHORT).show();
                item.isChecked = false
            }
            myAdapter.notifyDataSetChanged()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getMyData() {
        logging.level = HttpLoggingInterceptor.Level.BODY
        okHttpClientBuilder.addInterceptor(logging)
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