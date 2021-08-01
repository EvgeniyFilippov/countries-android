package com.example.course_android.ext

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.checkLocationPermission() =
    this.let {
        ContextCompat.checkSelfPermission(
            it,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    } == PackageManager.PERMISSION_GRANTED

fun Activity.askLocationPermission(locationPermissionCode: Int) {
    requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
}