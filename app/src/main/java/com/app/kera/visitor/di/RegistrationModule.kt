package com.app.kera.visitor.di

import com.app.kera.visitor.NeedToLoginViewModel
import com.app.kera.visitor.VisitorMainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val visitorModule = module {
    viewModel {
        VisitorMainViewModel(get())
    }
    viewModel {
        NeedToLoginViewModel(get())
    }
}