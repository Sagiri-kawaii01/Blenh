package com.github.sagiri_kawaii01.blenh.ui.screen.dashboard

import com.github.sagiri_kawaii01.blenh.model.TimePeriodType
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.model.bean.IconBean

internal sealed interface DashboardStateChange {
    fun reduce(oldState: DashboardState): DashboardState

    data object LoadingDialog : DashboardStateChange {
        override fun reduce(oldState: DashboardState): DashboardState = oldState.copy(loadingDialog = true)
    }

    sealed interface BillList: DashboardStateChange {
        override fun reduce(oldState: DashboardState): DashboardState {
            return when (this) {
                Loading -> oldState.copy(
                    dashboardListState = oldState.dashboardListState.apply { loading = true }
                )

                is Success -> oldState.copy(
                    dashboardListState = DashboardListState.Success(
                        type = type,
                        expend = expend,
                        income = income,
                        billList = billList,
                        iconList = iconList
                    ).apply { loading = true }
                )
            }
        }

        data class Success(
            val type: TimePeriodType,
            val expend: Double,
            val income: Double,
            val billList: List<BillBean>,
            val iconList: List<IconBean>
        ): BillList

        data object Loading: BillList
    }
}