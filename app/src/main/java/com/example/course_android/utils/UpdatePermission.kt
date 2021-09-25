package com.example.course_android.utils

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

fun Fragment.createLocationPermissionRequest(
    context: Context?,
    //if received permissions
    doOnSuccess: () -> Unit,
    //if NOT received permissions
    doOnReject: () -> Unit
): ActivityResultLauncher<Array<String>>? {

    context?.let {
        return this.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionData ->
            Log.e("permission check", permissionData.toString())
            if (permissionData[ACCESS_FINE_LOCATION] == true
                && permissionData[ACCESS_COARSE_LOCATION] == true
            ) {
                if (ActivityCompat.checkSelfPermission(
                        it,
                        ACCESS_FINE_LOCATION
                    )
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        it, ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    doOnSuccess.invoke()
                }
            } else {
                doOnReject.invoke()
                Log.e("permission check", "no permissions")
            }
        }
    }
    return null
}

fun ActivityResultLauncher<Array<String>>.requestLocationPermissions() {
    this.launch(
        arrayOf(
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION
        )
    )
}