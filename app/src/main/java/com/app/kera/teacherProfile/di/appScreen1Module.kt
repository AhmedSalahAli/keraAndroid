package com.app.kera.teacherProfile.di

import com.app.kera.teacherProfile.TeacherProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val teacherProfileModule = module {
    viewModel {
        TeacherProfileViewModel(get())
    }
}