package com.app.kera.registration.di

import com.app.kera.registration.screen1.Registration1ViewModel
import com.app.kera.registration.screen2.Registration2ViewModel
import com.app.kera.registration.screen3.Registration3ViewModel
import com.app.kera.registration.screen4.Registration4ViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val registrationModule = module {
    viewModel {
        Registration1ViewModel(get())
    }
    viewModel {
        Registration2ViewModel(get())
    }
    viewModel {
        Registration3ViewModel(get())
    }
    viewModel {
        Registration4ViewModel(get())
    }
}