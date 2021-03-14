package com.example.kera.app

import com.example.kera.main.ui.MainViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel


val viewModelModule = module {

    viewModel {
        MainViewModel(get())
    }
}