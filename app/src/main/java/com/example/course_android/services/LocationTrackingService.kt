package com.example.course_android.services

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.course_android.Constants.DEFAULT_DOUBLE
import com.example.course_android.Constants.DEFAULT_LONG
import com.example.course_android.Constants.MIN_TIME_BW_UPDATES_VALUE
import com.example.course_android.Constants.NEW_LOCATION_ACTION_VALUE
import com.example.course_android.Constants.SERVICE_ID_VALUE
import com.example.course_android.R

class LocationTrackingService : Service(), LocationListener {

    companion object {
        const val SERVICE_ID = SERVICE_ID_VALUE
        const val MIN_DISTANCE_CHANGE_FOR_UPDATES = DEFAULT_LONG
        const val MIN_TIME_BW_UPDATES = MIN_TIME_BW_UPDATES_VALUE
        const val NEW_LOCATION_ACTION = NEW_LOCATION_ACTION_VALUE
    }

    var mCheckIsGPSTurnedOn = false
    var mCheckNetworkIsTurnedOn = false
    var mCanGetLocation = false
    var mLocation: Location? = null
    var mLatitude = DEFAULT_DOUBLE
    var mLongitude = DEFAULT_DOUBLE

    protected var mLocationManager: LocationManager? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            if (!intent.hasExtra("kill_self")) {
                initLocationScan()
                initNotification()
            } else {
                killSelf()
            }
        }
        return super.onStartCommand(intent, flags, startId)
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

        builder.apply {
            this
                .setStyle(bigTextStyle)
                .setWhen(System.currentTimeMillis())
                .setFullScreenIntent(pendingIntent, true)
                .setSmallIcon(R.drawable.ic_baseline_navigation_24)
                .setContentText(getString(R.string.service_notification_text))
        }

        //build notification
        val notification: Notification = builder.build()

        //start foreground service
        startForeground(SERVICE_ID, notification)

    }

    @SuppressLint("MissingPermission")
    private fun initLocationScan(): Location? {
        try {
            mLocationManager =
                applicationContext?.getSystemService(LOCATION_SERVICE) as LocationManager

            //get gps status
            mCheckIsGPSTurnedOn =
                mLocationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true

            //get network provider status
            mCheckNetworkIsTurnedOn =
                mLocationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true

            if (!mCheckIsGPSTurnedOn) {
                Log.e("YF service: ", "gps turned off")
            } else {
                mCanGetLocation = true
                applicationContext?.let {
                    if (mCheckIsGPSTurnedOn) {
                        if (ContextCompat.checkSelfPermission(
                                it, ACCESS_FINE_LOCATION
                            ) == PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(
                                it,
                                ACCESS_COARSE_LOCATION
                            ) == PERMISSION_GRANTED
                        ) {
                            mLocationManager?.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
                            )
                            if (mLocationManager != null) {
                                mLocation =
                                    mLocationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
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

    @SuppressLint("MissingPermission")
    private fun stopListening() {
        if (mLocationManager != null) {
            mLocationManager?.let { manager ->
                applicationContext?.let {
                    manager.removeUpdates(this@LocationTrackingService)
                }
            }
        }
    }

    private fun killSelf() {
        stopListening()
        stopForeground(true)
        stopSelf()
    }

    //kill WITH lifecycle app
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        killSelf()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onLocationChanged(location: Location) {
        val intent = Intent()
        intent.action = NEW_LOCATION_ACTION
        intent.putExtra("location", location)
        sendBroadcast(intent)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }
}