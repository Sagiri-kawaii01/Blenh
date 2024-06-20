package com.github.sagiri_kawaii01.blenh.ui.screen.billlist

import com.github.sagiri_kawaii01.blenh.base.mvi.MviViewState
import com.github.sagiri_kawaii01.blenh.model.TimePeriodType

data class BillListState(
    val loadingDialog: Boolean,
    val billListDataState: BillListDataState
): MviViewState {
    companion object {
        fun initial() = BillListState(
            loadingDialog = false,
            billListDataState = BillListDataState.Init
        )
    }
}

sealed class BillListDataState {
    var loading: Boolean = false

    data object Init: BillListDataState()
    data class Success(
        val type: TimePeriodType,
        val billList: List<BillRecord>,

    ): BillListDataState()
}

data class BillRecord(
    val id: Int,
    val date: String,
    val time: String,
    val money: Double,
    val label: String,
    val iconIdx: Int,
    val payType: Int,
)