package com.example.sns.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.sns.base.BaseViewModel
import com.example.sns.retrofit.Dao
import com.example.sns.model.LoginBody
import com.example.sns.model.LoginData
import com.example.sns.model.RegisterBody
import com.example.sns.widget.MyApplication
import com.example.sns.widget.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LoginViewModel : BaseViewModel(){

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val registerBtn = SingleLiveEvent<Unit>()
    val loginBtn = SingleLiveEvent<Unit>()

    var status = MutableLiveData<String>()

    lateinit var myAPI : Dao
    lateinit var retrofit: Retrofit

    fun login() {
        myAPI = retrofit.create(Dao::class.java)
        myAPI.login(LoginBody(email = email.value.toString(), password = password.value.toString())).enqueue(object :
            Callback<LoginData> {
            override fun onFailure(call: Call<LoginData>, t: Throwable) {
                status.value = "400"
            }
            override fun onResponse(call: Call<LoginData>, response: Response<LoginData>) {
                status.value = response.code().toString()
                Log.d("TAG", "메시지 : ${response.message()}")
                Log.d("TAG","이유 : ${response.errorBody()?.string().toString()}")
                MyApplication.prefs.setToken("token", response.body()?.token.toString())
            }
        })
    }

    fun rBtnClick(){
        registerBtn.call()
    }

    fun lBtnClick(){
        loginBtn.call()
    }
}