package com.example.kera.teacherDailyReport.di

import com.example.kera.teacherDailyReport.ui.TeacherDailyReportViewModel
import com.example.kera.teacherDailyReport.writeReport.WriteReportViewModel
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