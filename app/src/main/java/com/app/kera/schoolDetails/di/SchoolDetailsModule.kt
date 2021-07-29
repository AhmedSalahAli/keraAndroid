package com.app.kera.schoolDetails.di

import com.app.kera.schoolDetails.ui.SchoolDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val schoolDetailsModule = module {
    viewModel {
        SchoolDetailsViewModel(get())
    }
}