package com.github.sagiri_kawaii01.blenh.ui.screen.edit

import com.github.sagiri_kawaii01.blenh.base.mvi.MviSingleEvent

sealed interface EditEvent: MviSingleEvent {
    data object SaveSuccess: EditEvent
}