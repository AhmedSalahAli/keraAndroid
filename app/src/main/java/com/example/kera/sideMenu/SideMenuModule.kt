package com.example.kera.sideMenu

import com.example.kera.schoolDetails.ui.SchoolDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val sideMenuModule = module {
    viewModel {
        SideMenuViewModel(get())
    }
}