package com.example.course_android.base.googlemap

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log
import com.example.course_android.dto.model.CountryDescriptionItemDto
import com.example.course_android.model.allCountries.CountriesDataItem
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions

private lateinit var currentCountryLatLng: LatLng
private var googleMap: GoogleMap? = null

private const val LOG_TAG = "CountryDetailsFragment"
private var distance: Int = 0

@SuppressLint("MissingPermission")
fun initMap2(
    map: GoogleMap,
    countryDetailsDto: CountryDescriptionItemDto,
    context: Context,
    permissionGps: Boolean
) {
    currentCountryLatLng = LatLng(
        countryDetailsDto.latlng[0],
        countryDetailsDto.latlng[1]
    )
    googleMap = map.apply {

        val cameraLocation = CameraUpdateFactory.newLatLngZoom(currentCountryLatLng, 7.0f)
        moveCamera(cameraLocation)
        if (permissionGps) {
            isMyLocationEnabled = true
            calculateDistance(context)
        }
    }
    addMarkerOnMap(currentCountryLatLng, countryDetailsDto.name)
}

//добавляем маркер
private fun addMarkerOnMap(markerPosition: LatLng, mCountryName: String) {
    val markerOptions = MarkerOptions()
        .position(markerPosition)
        .title(mCountryName)
    googleMap?.addMarker(markerOptions)
}

@SuppressLint("MissingPermission")
private fun calculateDistance(context: Context) {
    val currentCountryLocation = Location(LocationManager.GPS_PROVIDER).apply {
        latitude = currentCountryLatLng.latitude
        longitude = currentCountryLatLng.longitude
    }
    LocationServices.getFusedLocationProviderClient(context)
        .lastLocation
        .addOnSuccessListener { location ->
            distance = location.distanceTo(currentCountryLocation).toInt() / 1000
            Log.d(LOG_TAG, location.toString())
        }
}

fun getDistance(): Int = distance

@SuppressLint("MissingPermission")
fun initMap3(map: GoogleMap, listOfCountries: List<CountriesDataItem>) {
    googleMap = map

    listOfCountries.forEach { country ->
        if (country.latlng.size == 2) {
            addMarkerOnMap(
                LatLng(
                    country.latlng[0],
                    country.latlng[1]
                ), country.name
            )
        }
    }
}