package com.example.kera.home.di

import com.example.kera.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel {
        HomeViewModel(get())
    }
}