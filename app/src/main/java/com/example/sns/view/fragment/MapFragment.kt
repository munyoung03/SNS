package com.example.sns.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.sns.R
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentMainpageBinding
import com.example.sns.databinding.FragmentMapBinding
import com.example.sns.viewModel.MainPageViewModel
import com.example.sns.viewModel.MapViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MapFragment : BaseFragment<FragmentMapBinding, MapViewModel>() {

    override val viewModel: MapViewModel
        get() = getViewModel()

    override val layoutRes: Int
        get() = R.layout.fragment_map

    override fun init() {

    }

    override fun observerViewModel() {

    }
}