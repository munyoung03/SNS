package com.example.sns.retrofit

import com.example.sns.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Dao {
    @POST("/auth/login")
    fun login(
        @Body loginBody: LoginBody
    ) : Call<LoginData>

    @POST("/auth/registration")
    fun register(
        @Body registerBody : RegisterBody
    ) :Call<RegisterData>
}