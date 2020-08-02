package com.example.sns.retrofit

import retrofit2.http.Body

data class Body(
    val name: String,
    val email: String,
    val password: String,
    val passwordCheck: String
)