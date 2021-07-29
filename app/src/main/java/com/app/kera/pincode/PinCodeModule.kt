package com.app.kera.pincode

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val pinCodeModule = module {
    viewModel {
        PinCodeViewModel(get())
    }
}