package com.app.kera.dailyReport.di

import com.app.kera.dailyReport.ui.DailyReportViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dailyReportModule = module {
    viewModel {
        DailyReportViewModel(get())
    }
}