package com.app.kera.events.di

import com.app.kera.events.ui.EventsViewModel
import com.app.kera.events.ui.eventsdetails.EventsDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val eventsModule = module {
    viewModel {
        EventsViewModel(get())
    }
    viewModel {
        EventsDetailsViewModel(get())
    }

}