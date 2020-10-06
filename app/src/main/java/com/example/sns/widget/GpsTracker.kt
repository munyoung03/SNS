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
import com.google.android.gms.maps.model.Marker


class GpsTracker(mapFragment: MapFragment) : Service(), LocationListener {
    private var mContext: Context? = null
    private var location: Location? = null
    private var latitude = 0.0
    private var longitude = 0.0

    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10
    private val MIN_TIME_BW_UPDATES = 1000 * 60 * 1.toLong()
    protected var locationManager: LocationManager? = null
    private var callback : ((Double, Double) -> Unit)? = null;
    fun setCallback(callback : ((Double , Double)-> Unit)) {
        this.callback = callback;
    }
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
                Log.d("TAG", "HAHAHAHA")
                locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)

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

    override fun onLocationChanged(location: Location?) {
        Log.d("TAG", "HEHE ${location?.longitude}")
        if(location != null){ callback?.invoke(location.latitude, location.longitude)
        locationManager!!.removeUpdates(this)}
    }

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