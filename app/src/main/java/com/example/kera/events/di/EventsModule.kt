package com.example.kera.events.di

import com.example.kera.events.ui.EventsViewModel
import com.example.kera.events.ui.eventsdetails.EventsDetailsActivity
import com.example.kera.events.ui.eventsdetails.EventsDetailsViewModel
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