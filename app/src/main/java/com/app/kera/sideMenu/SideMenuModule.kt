package com.app.kera.sideMenu

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val sideMenuModule = module {
    viewModel {
        SideMenuViewModel(get())
    }
}