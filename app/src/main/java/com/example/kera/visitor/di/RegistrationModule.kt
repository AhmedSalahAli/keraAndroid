package com.example.kera.visitor.di

import com.example.kera.visitor.VisitorMainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val visitorModule = module {
    viewModel {
        VisitorMainViewModel(get())
    }

}