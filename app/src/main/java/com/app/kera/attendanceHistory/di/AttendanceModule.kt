package com.app.kera.attendanceHistory.di

import com.app.kera.attendanceHistory.AttendanceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AttendanceModule = module {
    viewModel {
        AttendanceViewModel(get())
    }

}