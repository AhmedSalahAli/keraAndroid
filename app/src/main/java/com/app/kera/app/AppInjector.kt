package com.app.kera.app

import com.app.kera.keraLuncher.KeraLuncherViewModel
import com.app.kera.main.ui.MainViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel


val viewModelModule = module {
    viewModel {
        KeraLuncherViewModel(get())
    }
    viewModel {
        MainViewModel(get())
    }

}