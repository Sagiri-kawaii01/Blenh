package com.github.sagiri_kawaii01.blenh.ui.screen.billlist

import com.github.sagiri_kawaii01.blenh.model.TimePeriodType
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean

interface BillListStateChange {
    fun reduce(state: BillListState): BillListState

    sealed interface BillList: BillListStateChange {
        override fun reduce(state: BillListState): BillListState {
            return when (this) {
                is Success -> {
                    state.copy(
                        billListDataState = BillListDataState.Success(
                            type = type,
                            billList = billList
                        )
                    )
                }
                Loading -> {
                    state.copy(loadingDialog = true)
                }
            }
        }

        data class Success(
            val type: TimePeriodType,
            val billList: List<BillRecord>
        ): BillList

        data object Loading: BillList
    }
}