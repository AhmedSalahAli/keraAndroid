package com.example.kera.medical.di

import com.example.kera.medical.MedicalReportViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val medicalModule = module {
    viewModel {
        MedicalReportViewModel(get())
    }
}