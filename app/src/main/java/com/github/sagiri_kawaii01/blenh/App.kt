package com.github.sagiri_kawaii01.blenh

import android.app.Application
import android.content.Context
import com.github.sagiri_kawaii01.blenh.util.CrashHandler
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
        CrashHandler.init(this)
    }


}

lateinit var appContext: Context