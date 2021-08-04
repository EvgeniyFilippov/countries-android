package com.example.course_android.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log
import com.example.course_android.dto.model.CountryDescriptionItemDto
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions


private lateinit var currentCountryLatLng: LatLng
private var googleMap: GoogleMap? = null

private const val LOG_TAG = "CountryDetailsFragment"
private var distance: Int = 0
private var currentLocationOfUser = Location("")

@SuppressLint("MissingPermission")
fun initMapOfCountryDetails(
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

//@SuppressLint("MissingPermission")
//private fun calculateDistance(context: Context) {
//    val currentCountryLocation = Location(LocationManager.GPS_PROVIDER).apply {
//        latitude = currentCountryLatLng.latitude
//        longitude = currentCountryLatLng.longitude
//    }
//    LocationServices.getFusedLocationProviderClient(context)
//        .lastLocation
//        .addOnSuccessListener { location ->
//            distance = location.distanceTo(currentCountryLocation).toInt() / 1000
//            Log.d(LOG_TAG, location.toString())
//        }
//}






@SuppressLint("MissingPermission")
 private fun getCurrentLocation(context: Context) {
    val mLocationRequest = LocationRequest.create()
    mLocationRequest.interval = 60000
    mLocationRequest.fastestInterval = 5000
    mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                if (location != null) {
                    currentLocationOfUser = location
                    Log.d(LOG_TAG, location.toString())
                }
            }
        }
    }
    LocationServices.getFusedLocationProviderClient(context)
        .requestLocationUpdates(mLocationRequest, mLocationCallback, null)
}

fun getDistance2(context: Context): Int {
    getCurrentLocation(context)
    calculateDistance(currentLocationOfUser)
    return distance
}

@SuppressLint("MissingPermission")
fun calculateDistance(location: Location) {
    val currentCountryLocation = Location(LocationManager.GPS_PROVIDER).apply {
        latitude = currentCountryLatLng.latitude
        longitude = currentCountryLatLng.longitude
    }

                    distance = location.distanceTo(currentCountryLocation).toInt() / 1000

}

fun getDistance(): Int = distance

@SuppressLint("MissingPermission")
fun initMapOfAllCountries(map: GoogleMap, listOfCountries: List<CountryDescriptionItemDto>) {
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