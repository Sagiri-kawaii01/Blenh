package com.github.sagiri_kawaii01.blenh.ui.screen.dashboard

import android.util.Log
import com.github.sagiri_kawaii01.blenh.model.TimePeriodType
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.model.bean.IconBean
import com.github.sagiri_kawaii01.blenh.model.vo.BillChart

internal sealed interface DashboardStateChange {
    fun reduce(oldState: DashboardState): DashboardState

    data object LoadingDialog : DashboardStateChange {
        override fun reduce(oldState: DashboardState): DashboardState = oldState.copy(loadingDialog = true)
    }

    sealed interface BillList: DashboardStateChange {
        override fun reduce(oldState: DashboardState): DashboardState {
            return when (this) {
                is Success -> oldState.copy(
                    dashboardListState = DashboardListState.Success(
                        type = type,
                        expend = expend,
                        income = income,
                        billList = billList,
                        charts = charts,
                        typeNameMap = typeNameMap
                    ),
                    loadingDialog = false
                )
            }
        }

        data class Success(
            val type: TimePeriodType,
            val expend: Pair<Double, Double>,
            val income: Pair<Double, Double>,
            val billList: List<BillBean>,
            val charts: List<BillChart>,
            val typeNameMap: Map<Int, String> = emptyMap()
        ): BillList

    }
}