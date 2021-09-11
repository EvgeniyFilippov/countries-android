package com.example.course_android.services

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.course_android.R

class LocationTrackingService : Service(), LocationListener {

    companion object {
        const val SERVICE_ID = 12321
        const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 0
        const val MIN_TIME_BW_UPDATES = (1000 * 10 * 1).toLong()
        const val NEW_LOCATION_ACTION = "NEW_LOCATION_ACTION"
    }

    var mCheckIsGPSTurnedOn = false
    var mCheckNetworkIsTurnedOn = false
    var mCanGetLocation = false
    var mLocation: Location? = null
    var mLatitude = 0.0
    var mLongitude = 0.0

    protected var mLocationManager: LocationManager? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)

        intent?.let {
            if (!intent.hasExtra("kill_self")) {
                initLocationScan()
                initNotification()
            } else {
                killSelf()
            }
        }
    }

    private fun initNotification() {
        val intent = Intent()
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.foreground_channel_name)
            val importance = NotificationManager.IMPORTANCE_MIN
            val mChannel =
                NotificationChannel(getString(R.string.foreground_channel_id), name, importance)

            val notificationManager = getSystemService(
                NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        //notification builder
        val builder = NotificationCompat.Builder(
            this,
            getString(R.string.foreground_channel_id)
        )

        //text style notification
        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.setBigContentTitle(getString(R.string.foreground_notification_name))
        builder.setStyle(bigTextStyle)
        builder.setWhen(System.currentTimeMillis())

        //head-up notification
        builder.setFullScreenIntent(pendingIntent, true)

        //build notification
        val notification: Notification = builder.build()

        //start foreground service
        startForeground(SERVICE_ID, notification)

    }

    private fun initLocationScan(): Location? {
        try {
            mLocationManager = applicationContext?.getSystemService(LOCATION_SERVICE) as LocationManager

            //get gps status
            mCheckIsGPSTurnedOn = mLocationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true

            //get network provider status
            mCheckIsGPSTurnedOn = mLocationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true

            if (!mCheckIsGPSTurnedOn) {
                Log.e("hz", "gps turned off")
            } else {
                mCanGetLocation = true
                applicationContext?.let {
                    if(mCheckIsGPSTurnedOn) {
                        if (ContextCompat.checkSelfPermission(
                                it,
                                Manifest.permission.ACCESS_FINE_LOCATION

                            ) == PackageManager.PERMISSION_GRANTED
                                    && ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED)
                        {
                            mLocationManager?.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
                            )
                            if (mLocationManager != null) {
                                mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                                if (mLocation != null) {
                                    mLatitude = mLocation?.latitude ?: 0.0
                                    mLongitude = mLocation?.longitude ?: 0.0
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mLocation
    }



    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onLocationChanged(location: Location) {
        TODO("Not yet implemented")
    }
}