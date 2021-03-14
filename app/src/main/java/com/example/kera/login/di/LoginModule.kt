package com.example.kera.login.di

import com.example.kera.login.ui.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    viewModel {
        LoginViewModel(get())
    }
}