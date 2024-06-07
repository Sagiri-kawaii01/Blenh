package com.github.sagiri_kawaii01.blenh.ui.screen.dashboard

import com.github.sagiri_kawaii01.blenh.base.mvi.MviViewState
import com.github.sagiri_kawaii01.blenh.model.TimePeriodType
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.model.bean.IconBean

data class DashboardState(
    val loadingDialog: Boolean,
    val dashboardListState: DashboardListState
): MviViewState {
    companion object {
        fun initial() = DashboardState(
            loadingDialog = false,
            dashboardListState = DashboardListState.Init
        )
    }
}

sealed class DashboardListState {
    var loading: Boolean = false

    data object Init: DashboardListState()
    data class Success(
        val type: TimePeriodType,
        val expend: Double,
        val income: Double,
        val billList: List<BillBean>,
        val iconList: List<IconBean>
    ): DashboardListState()
}
