package com.example.sns.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.sns.base.BaseViewModel
import com.example.sns.retrofit.Dao
import com.example.sns.retrofit.Login
import com.example.sns.widget.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LoginViewModel : BaseViewModel(){

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val body = HashMap<String, String>()

    val registerBtn = SingleLiveEvent<Unit>()
    val loginBtn = SingleLiveEvent<Unit>()

    var checkLogin : Boolean = false

    lateinit var myAPI : Dao
    lateinit var retrofit: Retrofit

    fun getlogindata() {
        body.put(email.value.toString(), password.value.toString())
        myAPI = retrofit.create(Dao::class.java)
        myAPI.getlogindata(body).enqueue(object :
            Callback<Login> {
            override fun onFailure(call: Call<Login>, t: Throwable) {
                checkLogin = false
            }
            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                checkLogin = true
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