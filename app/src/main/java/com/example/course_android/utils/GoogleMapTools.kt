package com.example.course_android.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log
import com.example.domain.dto.model.CountryDescriptionItemDto
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable

private lateinit var currentCountryLatLng: LatLng
private const val LOG_TAG = "CountryDetailsFragment"
private var distance: Int = 0
private var currentLocationOfUser = Location("")

@SuppressLint("MissingPermission")
fun getCurrentLocation(context: Context): Flowable<Location> {
    return Flowable.create<Location>({
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
                        it.onNext(location)
                        it.onComplete()
                        Log.d(LOG_TAG, location.toString())
                    } else {
                        it.onError(Exception())
                        it.onComplete()
                    }
                }
            }
        }
        LocationServices.getFusedLocationProviderClient(context)
            .requestLocationUpdates(mLocationRequest, mLocationCallback, null)
    }, BackpressureStrategy.LATEST)
}

fun getResultOfCurrentLocation(): Location {
    return currentLocationOfUser
}

fun getDistance(context: Context, country: CountryDescriptionItemDto): Int {
    currentCountryLatLng = LatLng(country.latlng[0], country.latlng[1])
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

@SuppressLint("MissingPermission")
fun getMyLocation(context: Context): Location {
        LocationServices.getFusedLocationProviderClient(context)
            .lastLocation
            .addOnSuccessListener { location ->
                currentLocationOfUser = location
            }
    return currentLocationOfUser
}