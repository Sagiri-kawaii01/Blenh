package com.github.sagiri_kawaii01.blenh.ui.screen.bottomsheet

import com.github.sagiri_kawaii01.blenh.base.mvi.MviSingleEvent

sealed interface BottomSheetEvent: MviSingleEvent {
    data object SaveSuccess: BottomSheetEvent
}