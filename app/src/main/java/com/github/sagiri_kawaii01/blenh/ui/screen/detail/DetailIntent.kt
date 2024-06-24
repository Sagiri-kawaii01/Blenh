package com.github.sagiri_kawaii01.blenh.ui.screen.detail

import com.github.sagiri_kawaii01.blenh.base.mvi.MviIntent

sealed interface DetailIntent: MviIntent {
    data class GetDetail(val billId: Int): DetailIntent
}