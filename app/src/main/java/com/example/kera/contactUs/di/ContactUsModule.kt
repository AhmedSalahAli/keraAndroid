package com.example.kera.contactUs.di

import com.example.kera.contactUs.ContactUsViewModel
import com.example.kera.education.EducationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val contactUsModule = module {
    viewModel {
        ContactUsViewModel(get())
    }
}