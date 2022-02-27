package com.app.kera.dailyReport.di

import com.app.kera.dailyReport.ui.DailyReportViewModel
import com.app.kera.dailyReport.ui.ReportDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val reportDetailsModule = module {
    viewModel {
        ReportDetailsViewModel(get())
    }
}