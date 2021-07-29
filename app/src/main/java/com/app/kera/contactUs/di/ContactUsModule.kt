package com.app.kera.contactUs.di

import com.app.kera.contactUs.ContactUsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val contactUsModule = module {
    viewModel {
        ContactUsViewModel(get())
    }
}