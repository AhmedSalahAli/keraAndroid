package com.example.kera.meals.di

import com.example.kera.meals.MealsViewModel
import com.example.kera.meals.details.MealsDetailsViewModel
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