package com.example.kera.app

import android.app.Application
import io.alterac.blurkit.BlurKit
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApp : Application() {

    companion object {
        lateinit var application: MyApp
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        BlurKit.init(this);
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApp)
            val modules = ArrayList(allFeatures)
            modules.addAll(listOf(viewModelModule))
            modules(
                modules
            )
        }

    }
}