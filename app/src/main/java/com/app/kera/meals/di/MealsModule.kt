package com.app.kera.meals.di

import com.app.kera.meals.MealsViewModel
import com.app.kera.meals.details.MealsDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mealsModule = module {
    viewModel {
        MealsViewModel(get())
    }
    viewModel {
        MealsDetailsViewModel(get())
    }
}