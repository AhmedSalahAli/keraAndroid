package com.app.kera.notification.di

import com.app.kera.notification.NotificationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val notificationModule = module {
    viewModel {
        NotificationViewModel(get())
    }
}