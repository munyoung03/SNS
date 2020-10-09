package com.example.sns.view.fragment

import android.app.Notification
import android.content.ContentValues.TAG
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentMainpageBinding
import com.example.sns.model.NotificationData
import com.example.sns.model.PushNotification
import com.example.sns.retrofit.RetrofitInstance
import com.example.sns.viewModel.MainPageViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_mainpage.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.lang.Exception

const val TOPIC = "/topics/myTopic"

class MainPageFragment : BaseFragment<FragmentMainpageBinding, MainPageViewModel>(){


    val TAG = "MainActivity"

    override val viewModel: MainPageViewModel
        get() = getViewModel(MainPageViewModel::class)

    override val layoutRes: Int
        get() = R.layout.fragment_mainpage

    override fun init() {
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
    }

    override fun observerViewModel() {
        with(viewModel){
            btn.observe(this@MainPageFragment, Observer {
                val title = title_edit.text.toString()
                val message = massage_edit.text.toString()
                if(title.isNotEmpty() && message.isNotEmpty()){
                    PushNotification(
                        NotificationData(title, message),
                        TOPIC
                        ).also {
                            sendNotification(it)
                    }
                }
            })
        }
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){
                Log.d(TAG, "Response : ${Gson().toJson(response)}")
            }else{
                Log.e(TAG, response.errorBody().toString())
            }

        }catch (e:Exception){
            Log.e(TAG, e.toString())
        }
    }

}