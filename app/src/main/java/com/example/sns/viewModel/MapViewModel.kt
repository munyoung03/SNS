package com.example.sns.viewModel

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sns.base.BaseViewModel
import com.example.sns.view.activity.MainActivity
import com.example.sns.widget.extension.startActivity
import com.example.sns.widget.extension.toast
import org.koin.dsl.koinApplication
import java.io.IOException
import java.util.*

class MapViewModel(private val application : Application) : BaseViewModel() {

    val checkDialogEvent = MutableLiveData<Boolean>()



    fun checkLocationServicesStatus(): Boolean {
        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        return (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }


}