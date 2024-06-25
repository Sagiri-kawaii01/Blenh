package com.github.sagiri_kawaii01.blenh.ui.screen.billlist

import com.github.sagiri_kawaii01.blenh.base.mvi.MviIntent
import com.github.sagiri_kawaii01.blenh.model.TimePeriodType

sealed interface BillListIntent: MviIntent {
    data class GetBillList(
        val type: TimePeriodType,
        val search: String = ""
    ): BillListIntent

    data class DeleteBill(
        val id: Int
    ): BillListIntent

    data object Init: BillListIntent
}