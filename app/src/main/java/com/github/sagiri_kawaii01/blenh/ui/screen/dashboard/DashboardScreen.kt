package com.github.sagiri_kawaii01.blenh.ui.screen.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.sagiri_kawaii01.blenh.base.mvi.getDispatcher
import com.github.sagiri_kawaii01.blenh.model.TimePeriodType
import com.github.sagiri_kawaii01.blenh.ui.component.DashConsume
import com.github.sagiri_kawaii01.blenh.ui.component.IOCard
import com.github.sagiri_kawaii01.blenh.ui.component.TendencyCard
import com.github.sagiri_kawaii01.blenh.util.format
import com.github.sagiri_kawaii01.blenh.util.today
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.viewState.collectAsState()
    val dispatcher = viewModel.getDispatcher(startWith = DashboardIntent.GetBillList(TimePeriodType.Week))

    uiState.dashboardListState

    val today = today()

    when (uiState.dashboardListState) {
        is DashboardListState.Success -> {
            val data = uiState.dashboardListState as DashboardListState.Success
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                
                IOCard(
                    label = "支出",
                    text = "￥${data.expend}",
                    modifier = Modifier
                        .height(120.dp)
                        .width(224.dp)
                        .background(Color.White)
                )

                Spacer(modifier = Modifier.height(12.dp))

                val sortedBills = data.billList.sortedBy { LocalDate.of(it.year, it.month, it.day).format() + it.time }

                TendencyCard(
                    label = when (data.type) {
                        TimePeriodType.Week -> "本周支出"
                        TimePeriodType.Month -> "本月支出"
                        TimePeriodType.Year -> "本年支出"
                        else -> "error"
                    },
                    values = data.billList.map { it.money.toFloat() },
                    keyName = numToMonthStr(sortedBills[0].month),
                    keys = sortedBills.map { it.day.toString() },
                    modifier = Modifier
                        .background(color = Color.White)
                        .height(343.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))
                
                DashConsume(
                    label = "消费记录",
                    billList = data.billList,
                    typeNameMap = emptyMap(),
                    modifier = Modifier.background(Color.White)
                )

            }
        }

        DashboardListState.Init -> {

        }
    }
}

private fun numToMonthStr(num: Int): String {
    return when (num) {
        1 -> "Jan"
        2 -> "Feb"
        3 -> "Mar"
        4 -> "Apr"
        5 -> "May"
        6 -> "Jun"
        7 -> "Jul"
        8 -> "Aug"
        9 -> "Sep"
        10 -> "Oct"
        11 -> "Nov"
        12 -> "Dec"
        else -> "error"
    }
}