package com.example.kera.navigation.di

import com.example.kera.navigation.NavigationViewModel
import com.example.kera.navigation.ui.NavigationMapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val navigationModule = module {
    viewModel {
        NavigationViewModel(get())
    }
    viewModel {
        NavigationMapViewModel(get())
    }
}