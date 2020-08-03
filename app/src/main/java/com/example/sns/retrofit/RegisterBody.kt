package com.example.sns.retrofit

import retrofit2.http.Body

data class Body(
    val username: String,
    val email: String,
    val password1: String,
    val password2: String
)