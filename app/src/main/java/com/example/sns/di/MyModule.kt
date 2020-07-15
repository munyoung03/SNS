package com.example.sns.di

import com.example.sns.viewModel.LoginViewModel
import com.example.sns.viewModel.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val MyModule = module {
    viewModel { LoginViewModel() }
    viewModel { RegisterViewModel() }
}