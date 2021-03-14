package com.example.kera.profile.di

import com.example.kera.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    viewModel {
        ProfileViewModel(get())
    }
}