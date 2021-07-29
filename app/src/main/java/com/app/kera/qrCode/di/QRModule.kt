package com.app.kera.qrCode.di

import com.app.kera.qrCode.QRViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val QRModule = module {
    viewModel {
        QRViewModel(get())
    }

}