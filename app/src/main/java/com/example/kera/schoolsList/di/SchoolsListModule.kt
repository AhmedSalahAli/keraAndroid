package com.example.kera.schoolsList.di

import com.example.kera.schoolsList.SchoolsListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val schoolsListModule = module {
    viewModel {
        SchoolsListViewModel(get())
    }
}