package com.example.amoz.app

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AmozApplication : Application() {
    companion object {
        private lateinit var appContext: Context

        fun getContext(): Context {
            return appContext
        }
    }

    override fun onCreate() {
        super.onCreate()

        appContext = this
    }
}


