package com.example.sns.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.sns.base.BaseViewModel
import com.example.sns.retrofit.Dao
import com.example.sns.retrofit.Login
import com.example.sns.retrofit.LoginBody
import com.example.sns.widget.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LoginViewModel : BaseViewModel(){

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val username = MutableLiveData<String>()

    val registerBtn = SingleLiveEvent<Unit>()
    val loginBtn = SingleLiveEvent<Unit>()

    var checkLogin = MutableLiveData<Boolean>()

    lateinit var myAPI : Dao
    lateinit var retrofit: Retrofit

    fun getlogindata() {
        myAPI = retrofit.create(Dao::class.java)
        myAPI.getlogindata(LoginBody(username = username.value.toString(), email = email.value.toString(), password = password.value.toString())).enqueue(object :
            Callback<Login> {
            override fun onFailure(call: Call<Login>, t: Throwable) {
                checkLogin.value = false
            }
            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                checkLogin.value = true
            }
        })
    }

    fun r_btnClick(){
        registerBtn.call()
    }

    fun l_btnClick(){
        loginBtn.call()
    }
}