package com.example.course_android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

//    https://newsapi.org/v2/top-headlines?country=us&apiKey=fe27628816ba4ca5b23fe932cf36e26e
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.nav_host)

        val newsApi = retrofit.create(NewsApi::class.java)
        val newsApiCall = newsApi.getTopHeadlines("us", "f12")
        newsApiCall.enqueue(object: Callback<NewsRootObject>{
            override fun onResponse(call: Call<NewsRootObject>, response: Response<NewsRootObject>) {
                Log.d("RETROFIT_COUNTRIES", response.body().toString())
            }

            override fun onFailure(call: Call<NewsRootObject>, t: Throwable) {
                Log.d("RETROFIT_COUNTRIES", t.toString())
            }
        })
    }
}