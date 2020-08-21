package com.example.sns.widget

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.sns.view.fragment.MapFragment


class GpsTracker(mapFragment: MapFragment) : Service(), LocationListener {
    private var mContext: Context? = null
    private var location: Location? = null
    private var latitude = 0.0
    private var longitude = 0.0

    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10
    private val MIN_TIME_BW_UPDATES = 1000 * 60 * 1.toLong()
    protected var locationManager: LocationManager? = null


    fun GpsTracker(context: Context?) {
        mContext = context
        getLocation()
    }


     fun getLocation(): Location? {
        try {
            locationManager = mContext?.getSystemService(LOCATION_SERVICE) as LocationManager?
            val isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGPSEnabled && !isNetworkEnabled) {
            } else {
                val hasFineLocationPermission = ContextCompat.checkSelfPermission(
                    mContext!!,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
                val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
                    mContext!!,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                    hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED
                ) {
                } else return null
                if (isNetworkEnabled) {
                    locationManager!!.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                        this
                    )
                    if (locationManager != null) {
                        location =
                            locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (location != null) {
                            latitude = location!!.latitude
                            longitude = location!!.longitude
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager!!.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                            this
                        )
                        if (locationManager != null) {
                            location =
                                locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if (location != null) {
                                latitude = location!!.latitude
                                longitude = location!!.longitude
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("@@@", "" + e.toString())
        }
        return location
    }

    fun getLatitude(): Double {
        if (location != null) {
            latitude = location!!.latitude
        }
        return latitude
    }

    fun getLongitude(): Double {
        if (location != null) {
            longitude = location!!.longitude
        }
        return longitude
    }

    override fun onLocationChanged(location: Location?) {}

    override fun onProviderDisabled(provider: String?) {}

    override fun onProviderEnabled(provider: String?) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onBind(arg0: Intent?): IBinder? {
        return null
    }


    fun stopUsingGPS() {
        if (locationManager != null) {
            locationManager!!.removeUpdates(this@GpsTracker)
        }
    }
}