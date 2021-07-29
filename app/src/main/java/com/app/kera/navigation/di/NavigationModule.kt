package com.app.kera.navigation.di

import com.app.kera.navigation.NavigationViewModel
import com.app.kera.navigation.ui.NavigationMapViewModel
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