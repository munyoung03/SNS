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

     fun getLocation(context: Context): Location? {
         mContext = context
         Log.d("TAG","NOT ENABLED")
        try {
            Log.d("TAG","NOT ENABLED")
            locationManager = mContext?.getSystemService(LOCATION_SERVICE) as LocationManager?
            val isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.d("TAG","NOT ENABLED")
            } else {
                val hasFineLocationPermission = ContextCompat.checkSelfPermission(
                    mContext!!,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
                val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
                    mContext!!,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
                print(hasFineLocationPermission)
                print(hasCoarseLocationPermission)
                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                    hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d("TAG","GRANTED")
                } else return null
                if (isNetworkEnabled) {
                    Log.d("TAG","ENABLED NET")
                    locationManager!!.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                        this
                    )
                    if (locationManager != null) {
                        Log.d("TAG","NOT NULL")
                        location =
                            locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (location != null) {
                            latitude = location!!.latitude
                            longitude = location!!.longitude
                            Log.d("TAG", "경도1 : $latitude, 위도1 : $longitude")
                        }
                    }
                }
                if (isGPSEnabled) {
                    Log.d("TAG","ENABLED GPS")
                    if (location == null) {
                        Log.d("TAG","NOT NULL LOC")
                        locationManager!!.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                            this
                        )
                        if (locationManager != null) {

                            Log.d("TAG","NOT NULL MANAGER")
                            location =
                                locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if (location != null) {
                                latitude = location!!.latitude
                                longitude = location!!.longitude
                                Log.d("TAG", "경도2 : $latitude, 위도2 : $longitude")
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