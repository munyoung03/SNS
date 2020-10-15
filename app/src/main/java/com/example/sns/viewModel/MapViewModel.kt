package com.example.sns.viewModel

import android.app.Application
import android.content.Context
import android.location.LocationManager
import com.example.sns.base.BaseViewModel

class MapViewModel(private val application: Application) : BaseViewModel() {

    fun checkLocationServicesStatus(): Boolean {
        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        return (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }


}