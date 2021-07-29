package com.app.kera.teacherDailyReport.di

import com.app.kera.teacherDailyReport.ui.TeacherDailyReportViewModel
import com.app.kera.teacherDailyReport.writeReport.WriteReportViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val teacherDailyReportModule = module {
    viewModel {
        TeacherDailyReportViewModel(get())
    }
    viewModel {
        WriteReportViewModel(get())
    }
}