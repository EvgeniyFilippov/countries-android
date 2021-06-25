package com.example.course_android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import okhttp3.*
import java.io.IOException

private const val URL = "https://restcountries.eu/rest/v2/all"

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    private val httpClient = OkHttpClient()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getTopHeadLines()
        navController = Navigation.findNavController(this, R.id.nav_host)

    }

    private fun getTopHeadLines() {
        val request = Request.Builder()
            .url(URL)
            .build()
        val call = httpClient.newCall(request = request)
        call.enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("JSON", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val json = response.body?.string()
                    Log.d("JSON", json ?: "")
                }
            }

        })

    }
}