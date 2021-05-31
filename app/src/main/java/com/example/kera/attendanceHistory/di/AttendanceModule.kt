package com.example.kera.attendanceHistory.di

import com.example.kera.attendanceHistory.AttendanceViewModel
import com.example.kera.qrCode.QRViewModel
import com.example.kera.registration.screen1.Registration1ViewModel
import com.example.kera.registration.screen2.Registration2ViewModel
import com.example.kera.registration.screen3.Registration3ViewModel
import com.example.kera.registration.screen4.Registration4ViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AttendanceModule = module {
    viewModel {
        AttendanceViewModel(get())
    }

}