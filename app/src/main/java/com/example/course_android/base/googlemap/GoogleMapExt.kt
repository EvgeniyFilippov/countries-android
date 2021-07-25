package com.example.course_android.base.googlemap

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.example.course_android.Constants
import com.example.course_android.Constants.DISTANCE_KEY
import com.example.course_android.dto.model.CountryDescriptionItemDto

import com.google.android.gms.location.LocationServices
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions

private lateinit var currentCountryLatLng: LatLng
private var googleMap: GoogleMap? = null
private const val LOCATION_PERMISSION_CODE = 1000
private const val LOG_TAG = "CountryDetailsFragment"
private var distance: Int = 0

fun initMap2(map: GoogleMap, countryDetailsDto: CountryDescriptionItemDto, context: Context) {
    currentCountryLatLng = LatLng(
        countryDetailsDto.latlng[0],
        countryDetailsDto.latlng[1]
    )
    googleMap = map.apply {

        val cameraLocation = CameraUpdateFactory.newLatLngZoom(currentCountryLatLng, 7.0f)
        moveCamera(cameraLocation)
        if (checkLocationPermission(context)) {
            isMyLocationEnabled = true
            calculateDistance(context)
        } else {
//            askLocationPermission()
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

//проверяем permission
private fun checkLocationPermission(context: Context) =
    context.let {
        ContextCompat.checkSelfPermission(
            it,
            ACCESS_FINE_LOCATION
        )
    } == PermissionChecker.PERMISSION_GRANTED

////запрос permission
//private fun askLocationPermission() {
//    requestPermissions(arrayOf(ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE)
//}

//@SuppressLint("MissingPermission")
//fun onRequestPermissionsResult(
//    requestCode: Int,
//    permissions: Array<out String>,
//    grantResults: IntArray
//) {
//    if (requestCode == LOCATION_PERMISSION_CODE && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
//        googleMap?.isMyLocationEnabled = true
//        getDistance(con)
//    }
//    onRequestPermissionsResult(requestCode, permissions, grantResults)
//}

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

 fun getDistance() : Int = distance