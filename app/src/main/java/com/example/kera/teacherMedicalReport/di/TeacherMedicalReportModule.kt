package com.example.kera.teacherMedicalReport.di

import com.example.kera.teacherMedicalReport.TeacherMedicalReportViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val teacherMedicalReportModule = module {
    viewModel {
        TeacherMedicalReportViewModel(get())
    }
}