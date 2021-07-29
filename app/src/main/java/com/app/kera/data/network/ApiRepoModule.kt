package com.app.kera.data.network

import android.content.Context
import com.app.kera.preference.AppSharedPreference
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val apiRepoModule = module {
    factory {
        AppRepo(get())
    }
    single {
        androidApplication().getSharedPreferences("keraPreference", Context.MODE_PRIVATE)
    }
    single {
        AppSharedPreference(
            androidApplication(),
            "keraPreference"
        )
    }
}