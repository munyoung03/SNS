package com.example.sns.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.sns.base.BaseViewModel
import com.example.sns.retrofit.Dao
import com.example.sns.retrofit.Register
import com.example.sns.widget.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class RegisterViewModel : BaseViewModel(){
    val btn = SingleLiveEvent<Unit>()
    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    var checkLogin : Boolean? = null

    lateinit var myAPI : Dao
    lateinit var retrofit: Retrofit

    fun sendRegisterData(){
        myAPI = retrofit.create(Dao::class.java)
        myAPI.sendregister(email.value.toString(), password.value.toString(), name.value.toString()).enqueue(object :
            Callback<Register> {
            override fun onFailure(call: Call<Register>, t: Throwable) {
                checkLogin = false
            }

            override fun onResponse(call: Call<Register>, response: Response<Register>) {
                checkLogin = true
            }
        })
    }

    fun btnClick(){
        btn.call()
    }
}