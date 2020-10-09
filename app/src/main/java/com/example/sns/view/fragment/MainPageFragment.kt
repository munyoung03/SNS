package com.example.sns.view.fragment

import android.app.Notification
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentMainpageBinding
import com.example.sns.viewModel.MainPageViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.getViewModel


class MainPageFragment : BaseFragment<FragmentMainpageBinding, MainPageViewModel>(){

    override val viewModel: MainPageViewModel
        get() = getViewModel(MainPageViewModel::class)

    override val layoutRes: Int
        get() = R.layout.fragment_mainpage

    override fun init() {
    }

    override fun observerViewModel() {
        with(viewModel){
            pushBtn.observe(this@MainPageFragment, Observer {

            })
        }
    }


}