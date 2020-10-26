package com.example.sns.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.sns.base.BaseViewModel
import com.example.sns.model.RegisterBody
import com.example.sns.model.RegisterData
import com.example.sns.network.retrofit.RetrofitService
import com.example.sns.widget.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class RegisterViewModel : BaseViewModel() {
    val btn = SingleLiveEvent<Unit>()
    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val passwordCheck = MutableLiveData<String>()

    var status = MutableLiveData<String>()

    lateinit var myAPI: RetrofitService
    lateinit var retrofit: Retrofit

    fun register() {
        myAPI = retrofit.create(RetrofitService::class.java)
        myAPI.register(
            RegisterBody(
                username = name.value.toString(),
                email = email.value.toString(),
                password = password.value.toString()
            )
        ).enqueue(object :
            Callback<RegisterData> {
            override fun onFailure(call: Call<RegisterData>, t: Throwable) {
                status.value = "400"
            }

            override fun onResponse(call: Call<RegisterData>, response: Response<RegisterData>) {
                status.value = response.code().toString()
            }
        })
    }

    fun btnClick() {
        btn.call()
    }
}