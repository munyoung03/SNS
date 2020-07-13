package com.example.sns.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var instance: Retrofit? = null

    fun getInstance(): Retrofit {
        if (instance == null) {
            instance = Retrofit.Builder()
                .baseUrl("http://munyoung.kro.kr:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return instance!!
    }
}