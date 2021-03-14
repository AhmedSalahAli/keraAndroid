package com.example.kera.schoolDetails.di

import com.example.kera.schoolDetails.ui.SchoolDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val schoolDetailsModule = module {
    viewModel {
        SchoolDetailsViewModel(get())
    }
}