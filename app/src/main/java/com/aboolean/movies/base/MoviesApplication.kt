package com.aboolean.movies.base

import android.app.Application
import com.aboolean.movies.di.ApplicationModule
import com.aboolean.movies.di.NetworkModule
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MoviesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MoviesApplication)
            modules(listOf(NetworkModule, ApplicationModule))
        }
        Stetho.initializeWithDefaults(this)
    }
}