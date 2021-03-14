package com.example.kera.splash.di

import com.example.kera.splash.ui.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val splashModule = module {
    viewModel {
        SplashViewModel(get())
    }
}