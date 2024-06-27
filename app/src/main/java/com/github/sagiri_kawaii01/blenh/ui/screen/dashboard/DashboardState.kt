package com.github.sagiri_kawaii01.blenh.ui.screen.dashboard

import com.github.sagiri_kawaii01.blenh.base.mvi.MviViewState
import com.github.sagiri_kawaii01.blenh.model.TimePeriodType
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.model.bean.IconBean
import com.github.sagiri_kawaii01.blenh.model.vo.BillChart

data class DashboardState(
    val loadingDialog: Boolean,
    val dashboardListState: DashboardListState
): MviViewState {
    companion object {
        fun initial() = DashboardState(
            loadingDialog = true,
            dashboardListState = DashboardListState.Init
        )
    }
}

sealed class DashboardListState {

    data object Init: DashboardListState()
    data class Success(
        val type: TimePeriodType,
        val expend: Pair<Double, Double>,
        val income: Pair<Double, Double>,
        val billList: List<BillBean>,
        val charts: List<BillChart>,
        val typeNameMap: Map<Int, String> = emptyMap()
    ): DashboardListState()
}
