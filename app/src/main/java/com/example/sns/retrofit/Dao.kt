package com.example.sns.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface Dao {
    @POST("/auth/login/")
    fun getlogindata(
        @Body loginBody: LoginBody
    ) : Call<Login>

    @POST("/auth/registration/")
    fun sendregister(
        @Body body: com.example.sns.retrofit.Body
    ) :Call<Register>
}