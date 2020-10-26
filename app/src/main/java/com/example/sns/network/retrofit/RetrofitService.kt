package com.example.sns.network.retrofit

import com.example.sns.model.LoginBody
import com.example.sns.model.LoginData
import com.example.sns.model.RegisterBody
import com.example.sns.model.RegisterData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitService {
    @POST("/auth/login")
    fun login(
        @Body loginBody: LoginBody
    ): Call<LoginData>

    @POST("/auth/registration")
    fun register(
        @Body registerBody: RegisterBody,

    ): Call<RegisterData>
}