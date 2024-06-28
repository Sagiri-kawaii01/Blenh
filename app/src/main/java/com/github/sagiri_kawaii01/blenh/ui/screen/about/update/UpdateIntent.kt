package com.github.sagiri_kawaii01.blenh.ui.screen.about.update

import com.github.sagiri_kawaii01.blenh.base.mvi.MviIntent

sealed interface UpdateIntent : MviIntent {
    data class CheckUpdate(val isRetry: Boolean) : UpdateIntent
    data class Update(val url: String?) : UpdateIntent
}