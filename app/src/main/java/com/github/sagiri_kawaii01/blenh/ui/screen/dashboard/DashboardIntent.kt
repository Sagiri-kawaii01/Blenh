package com.github.sagiri_kawaii01.blenh.ui.screen.dashboard

import com.github.sagiri_kawaii01.blenh.base.mvi.MviIntent
import com.github.sagiri_kawaii01.blenh.model.TimePeriodType

sealed interface DashboardIntent: MviIntent {
    data class GetBillList(val type: TimePeriodType): DashboardIntent
    data class Delete(val id: Int): DashboardIntent
}

