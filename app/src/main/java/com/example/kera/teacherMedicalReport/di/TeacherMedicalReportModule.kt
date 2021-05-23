package com.example.kera.teacherMedicalReport.di

import com.example.kera.teacherMedicalReport.TeacherMedicalReportViewModel
import com.example.kera.teacherMedicalReport.writeMedicalReport.WriteMedicalReportViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val teacherMedicalReportModule = module {
    viewModel {
        TeacherMedicalReportViewModel(get())
    }
    viewModel {
        WriteMedicalReportViewModel(get())
    }
}