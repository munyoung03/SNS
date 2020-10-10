package com.example.sns.viewModel

import com.example.sns.base.BaseViewModel
import com.example.sns.widget.MyApplication
import com.example.sns.widget.SingleLiveEvent

class MyPageViewModel : BaseViewModel() {

    val logoutBtn = SingleLiveEvent<Unit>()
    val image = SingleLiveEvent<Unit>()

    fun setData(){
        MyApplication.prefs.setCheckLogin("checklogin", null)
        MyApplication.prefs.setUsername("name", null)
        MyApplication.prefs.setToken("token", null)
        MyApplication.prefs.setEmail("email", null)
    }

    fun logoutBtnClick(){
        logoutBtn.call()
    }

    fun imageClick(){
        image.call()
    }


}