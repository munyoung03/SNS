package com.example.sns.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.sns.base.BaseViewModel
import com.example.sns.retrofit.Body
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
    val passwordCheck = MutableLiveData<String>()

    var checkLogin = MutableLiveData<Boolean>()

    lateinit var myAPI : Dao
    lateinit var retrofit: Retrofit

    fun sendRegisterData(){
        myAPI = retrofit.create(Dao::class.java)
        myAPI.sendregister(Body(username = name.value.toString(), email = email.value.toString(), password1 = password.value.toString(), password2 = passwordCheck.value.toString()) ).enqueue(object :
            Callback<Register> {
            override fun onFailure(call: Call<Register>, t: Throwable) {
                checkLogin.value = false
                Log.d("ASD", "DDD");
            }

            override fun onResponse(call: Call<Register>, response: Response<Register>) {
                checkLogin.value = true
                Log.d("pk", "pk:"+response.body()?.user?.pk)
            }
        })
    }

    fun btnClick(){
        btn.call()
    }
}