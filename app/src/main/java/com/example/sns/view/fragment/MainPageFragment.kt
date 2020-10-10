package com.example.sns.view.fragment

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import com.example.sns.R
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentMainpageBinding
import com.example.sns.viewModel.MainPageViewModel
import com.example.sns.widget.extension.toast
import com.google.firebase.iid.FirebaseInstanceId
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.security.MessageDigest


class MainPageFragment : BaseFragment<FragmentMainpageBinding, MainPageViewModel>(){

    override val viewModel: MainPageViewModel
        get() = getViewModel(MainPageViewModel::class)

    override val layoutRes: Int
        get() = R.layout.fragment_mainpage

    override fun init() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener {
                if(!it.isSuccessful)
                {
                    Log.d("TAG", "getInstanceId failed${it.exception}")
                }
                else
                {
                    var token = it.result.token
                    Log.d("TAG", "내토큰 : $token")
                    toast(token)
                }
            }
    }

    override fun observerViewModel() {
        with(viewModel){

        }
    }

}