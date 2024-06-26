package com.github.sagiri_kawaii01.blenh.ui.screen.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.sagiri_kawaii01.blenh.base.mvi.getDispatcher
import com.github.sagiri_kawaii01.blenh.model.TimePeriodType
import com.github.sagiri_kawaii01.blenh.model.vo.BillChart
import com.github.sagiri_kawaii01.blenh.ui.component.DashConsume
import com.github.sagiri_kawaii01.blenh.ui.component.IOCard
import com.github.sagiri_kawaii01.blenh.ui.component.TendencyCard
import com.github.sagiri_kawaii01.blenh.ui.component.TimePeriodButton
import com.github.sagiri_kawaii01.blenh.ui.local.LocalNavController
import com.github.sagiri_kawaii01.blenh.ui.route.ROUTE_ADD_BILL
import com.github.sagiri_kawaii01.blenh.ui.theme.Gray20
import com.github.sagiri_kawaii01.blenh.util.today
import java.time.LocalDate

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.viewState.collectAsState()
    val dispatcher = viewModel.getDispatcher(startWith = DashboardIntent.GetBillList(TimePeriodType.Week))
    val navController = LocalNavController.current

    when (uiState.dashboardListState) {
        is DashboardListState.Success -> {
            val data = uiState.dashboardListState as DashboardListState.Success
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                floatingActionButton = {
                    FilledIconButton(
                        onClick = {
                            navController.navigate(ROUTE_ADD_BILL)
                        },
                        colors = IconButtonDefaults.filledIconButtonColors().copy(
                            containerColor = Gray20
                        )
                    ) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                    }
                },
                topBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                    ) {
                        TimePeriodButton("周", data.type == TimePeriodType.Week) {
                            dispatcher(DashboardIntent.GetBillList(TimePeriodType.Week))
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        TimePeriodButton("月", data.type == TimePeriodType.Month) {
                            dispatcher(DashboardIntent.GetBillList(TimePeriodType.Month))
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        TimePeriodButton("年", data.type == TimePeriodType.Year) {
                            dispatcher(DashboardIntent.GetBillList(TimePeriodType.Year))
                        }
                    }
                }
            ) {


                Column(modifier = Modifier.padding(it)) {

                    Spacer(modifier = Modifier.height(24.dp))

                    LazyRow {
                        item {
                            IOCard(
                                label = "支出",
                                text = "￥%.2f".format(data.expend.first),
                                description = ioCardDescription(data.expend, data.type),
                                modifier = Modifier
                                    .height(120.dp)
                                    .width(224.dp)
                                    .background(Color.White)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            IOCard(
                                label = "收入",
                                text = "￥%.2f".format(data.income.first),
                                description = ioCardDescription(data.income, data.type),
                                modifier = Modifier
                                    .height(120.dp)
                                    .width(224.dp)
                                    .background(Color.White)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val sortedBills = billsToCharts(data.charts, data.type)

                    TendencyCard(
                        label = when (data.type) {
                            TimePeriodType.Week -> "近期支出"
                            TimePeriodType.Month -> "本月支出"
                            TimePeriodType.Year -> "本年支出"
                            else -> "error"
                        },
                        values = sortedBills.first,
                        keyName = sortedBills.third,
                        keys = sortedBills.second,
                        modifier = Modifier
                            .background(color = Color.White)
                            .height(343.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DashConsume(
                        label = "消费记录",
                        billList = data.billList,
                        typeNameMap = data.typeNameMap,
                        modifier = Modifier.background(Color.White)
                    ) { bill ->
                        dispatcher.invoke(DashboardIntent.Delete(bill.id))
                    }
                }
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

private fun ioCardDescription(value: Pair<Double, Double>, type: TimePeriodType): String {
    val rate = (100.0 * (value.first - value.second) / value.second).toInt()
    val word = when (type) {
        TimePeriodType.Week -> "week over week"
        TimePeriodType.Month -> "month over month"
        TimePeriodType.Year -> "year over year"
        else -> "error"
    }
    return if (rate < 0) {
        "$rate% $word"
    } else {
        "+$rate% $word"
    }
}

private fun billsToWeek(bills: List<BillChart>): Triple<List<Double>, List<String>, String> {
    val values = Array<Double>(7) { 0.0 }
    val keys = Array<String>(7) { "" }
    var currentDay = today()
    val billMap = bills.associate { LocalDate.of(it.year, it.month, it.day!!) to it.money }
    for (i in 6 downTo 0) {
        keys[i] = currentDay.dayOfMonth.toString()
        values[i] = billMap[currentDay] ?: 0.0
        currentDay = currentDay.minusDays(1)
    }
    currentDay = currentDay.plusDays(1)
    return Triple(values.toList(), keys.toList(), numToMonthStr(currentDay.monthValue))
}

private fun billsToMonth(bills: List<BillChart>): Triple<List<Double>, List<String>, String> {
    val currentDay = today()
    var dayOfMonth = currentDay.dayOfMonth
    val values = Array<Double>(dayOfMonth) { 0.0 }
    val keys = Array<String>(dayOfMonth) { "" }
    val billMap = bills.associate { it.day to it.money }
    for (i in dayOfMonth - 1 downTo 0) {
        keys[i] = dayOfMonth.toString()
        values[i] = billMap[dayOfMonth--] ?: 0.0
    }
    return Triple(values.toList(), keys.toList(), numToMonthStr(currentDay.monthValue))
}

private fun billsToYear(bills: List<BillChart>): Triple<List<Double>, List<String>, String> {
    val today = today()
    var currentMonth = today.monthValue
    val values = Array<Double>(currentMonth) { 0.0 }
    val keys = Array<String>(currentMonth) { "" }
    val billMap = bills.associate { it.month to it.money }
    for (i in currentMonth - 1 downTo 0) {
        keys[i] = numToMonthStr(currentMonth)
        values[i] = billMap[currentMonth--] ?: 0.0
    }
    return Triple(values.toList(), keys.toList(), today.year.toString())
}

private fun billsToCharts(charts: List<BillChart>, type: TimePeriodType): Triple<List<Double>, List<String>, String> {
    return when (type) {
        TimePeriodType.Week -> billsToWeek(charts)
        TimePeriodType.Month -> billsToMonth(charts)
        TimePeriodType.Year -> billsToYear(charts)
        else -> Triple(listOf(), listOf(), "")
    }
}