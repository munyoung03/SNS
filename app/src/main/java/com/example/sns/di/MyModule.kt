package com.example.sns.di

import android.app.Application
import com.example.sns.viewModel.*
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val MyModule = module {
    viewModel { LoginViewModel() }
    viewModel { RegisterViewModel() }
    viewModel { MapViewModel(androidApplication()) }
    viewModel { MainPageViewModel() }
    viewModel { MyPageViewModel() }
}