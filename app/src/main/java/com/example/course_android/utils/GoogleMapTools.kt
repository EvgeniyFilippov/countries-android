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
private lateinit var currentCountryLatLngFilter: LatLng

@SuppressLint("MissingPermission")
fun initMapOfCountryDetails(
    map: GoogleMap,
    countryDetailsDto: CountryDescriptionItemDto,
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
 fun getCurrentLocation(context: Context) {
    LocationServices.getFusedLocationProviderClient(context)
        .lastLocation
        .addOnSuccessListener { location ->
            currentLocationOfUser = location
        }

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

fun getResultOfCurrentLocation(): Location {
    return currentLocationOfUser
}

fun getDistance(context: Context): Int {
    getCurrentLocation(context)
    calculateDistance(currentLocationOfUser)
    return distance
}

fun calculateDistance(location: Location) {
    val currentCountryLocation = Location(LocationManager.GPS_PROVIDER).apply {
        latitude = currentCountryLatLng.latitude
        longitude = currentCountryLatLng.longitude
    }
    distance = location.distanceTo(currentCountryLocation).toInt() / 1000
}

fun calculateDistanceFiler(location: Location, countryDetailsDto: CountryDescriptionItemDto): Int {
    val currentCountryLatLng = LatLng(
        countryDetailsDto.latlng[0],
        countryDetailsDto.latlng[1]
    )

    val currentCountryLocation = Location(LocationManager.GPS_PROVIDER).apply {
        latitude = currentCountryLatLng.latitude
        longitude = currentCountryLatLng.longitude
    }
    distance = location.distanceTo(currentCountryLocation).toInt() / 1000
    return distance
}

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