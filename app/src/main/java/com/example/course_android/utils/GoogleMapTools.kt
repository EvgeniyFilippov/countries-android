package com.example.course_android.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import com.example.course_android.Constants.N_A
import com.example.course_android.services.LocationTrackingService.Companion.mLocation
import com.example.domain.dto.model.CountryDescriptionItemDto
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

private lateinit var currentCountryLatLng: LatLng
private var distance: String = N_A

fun getDistance(country: CountryDescriptionItemDto): String {
    val location = mLocation

    currentCountryLatLng = LatLng(country.latlng[0], country.latlng[1])
    val currentCountryLocation = Location(LocationManager.GPS_PROVIDER).apply {
        latitude = currentCountryLatLng.latitude
        longitude = currentCountryLatLng.longitude
    }
    if (location != null) {
        distance = (location.distanceTo(currentCountryLocation).toInt() / 1000
        ).toString()
    } else {
        distance = N_A
        }

        return distance
    }


    fun calculateDistanceFiler(
        location: Location,
        countryDetailsDto: CountryDescriptionItemDto
    ): Int {
        val currentCountryLatLng = LatLng(
            countryDetailsDto.latlng[0],
            countryDetailsDto.latlng[1]
        )

        val currentCountryLocation = Location(LocationManager.GPS_PROVIDER).apply {
            latitude = currentCountryLatLng.latitude
            longitude = currentCountryLatLng.longitude
        }
        return location.distanceTo(currentCountryLocation).toInt() / 1000

    }

    @SuppressLint("MissingPermission")
    fun getLocationProviderClient(context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    fun getGeocoder(context: Context) = Geocoder(context)