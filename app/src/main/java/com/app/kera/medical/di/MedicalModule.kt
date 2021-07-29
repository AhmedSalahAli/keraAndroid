package com.app.kera.medical.di

import com.app.kera.medical.MedicalReportViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val medicalModule = module {
    viewModel {
        MedicalReportViewModel(get())
    }
}