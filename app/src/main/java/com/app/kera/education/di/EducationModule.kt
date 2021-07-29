package com.app.kera.education.di

import com.app.kera.education.EducationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val educationModule = module {
    viewModel {
        EducationViewModel(get())
    }
}