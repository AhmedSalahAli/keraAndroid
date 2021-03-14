package com.example.kera.education.di

import com.example.kera.education.EducationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val educationModule = module {
    viewModel {
        EducationViewModel(get())
    }
}