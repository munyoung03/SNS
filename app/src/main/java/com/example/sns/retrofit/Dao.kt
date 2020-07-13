package com.example.sns.retrofit

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface Dao {
    @POST("/login")
    fun getlogindata(
        @Query("email") email: String,
        @Query("password") password: String
    ) : Call<Login>

    @POST("/register")
    fun sendregister(
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("name") name: String
    ) :Call<Register>
}