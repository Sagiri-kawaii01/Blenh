package com.github.sagiri_kawaii01.blenh.ui.screen.about.update

import com.github.sagiri_kawaii01.blenh.base.mvi.MviSingleEvent
import kotlin.random.Random

sealed interface UpdateEvent : MviSingleEvent {
    data class CheckError(
        val msg: String,
        private val random: Long = Random.nextLong() + System.currentTimeMillis(),
    ) : UpdateEvent

    data class CheckSuccess(private val random: Long = Random.nextLong() + System.currentTimeMillis()) :
        UpdateEvent
}
