package com.app.kera.schoolsList.di

import com.app.kera.schoolsList.SchoolsListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val schoolsListModule = module {
    viewModel {
        SchoolsListViewModel(get())
    }
}