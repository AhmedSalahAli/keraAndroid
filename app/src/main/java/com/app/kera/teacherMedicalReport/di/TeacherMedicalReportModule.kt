package com.app.kera.teacherMedicalReport.di

import com.app.kera.teacherMedicalReport.TeacherMedicalReportViewModel
import com.app.kera.teacherMedicalReport.writeMedicalReport.WriteMedicalReportViewModel
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