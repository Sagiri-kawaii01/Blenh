package com.github.sagiri_kawaii01.blenh

import android.app.Application
import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import com.github.sagiri_kawaii01.blenh.model.db.AppDatabase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}

lateinit var appContext: Context