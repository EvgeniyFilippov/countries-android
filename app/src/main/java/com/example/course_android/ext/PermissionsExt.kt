package com.example.course_android.ext

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

fun Context.checkLocationPermission() =
    this.let {
        ContextCompat.checkSelfPermission(
            it,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

@SuppressLint("NewApi")
fun Activity.askLocationPermission(locationPermissionCode: Int) {
    requestPermissions(arrayOf(ACCESS_FINE_LOCATION), locationPermissionCode)
}