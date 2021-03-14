package com.example.kera.dailyReport.di

import com.example.kera.dailyReport.ui.DailyReportViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dailyReportModule = module {
    viewModel {
        DailyReportViewModel(get())
    }
}