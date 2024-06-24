package com.github.sagiri_kawaii01.blenh.ui.screen.billlist

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.sagiri_kawaii01.blenh.base.mvi.getDispatcher
import com.github.sagiri_kawaii01.blenh.model.TimePeriodType
import com.github.sagiri_kawaii01.blenh.model.bean.IconBean
import com.github.sagiri_kawaii01.blenh.ui.component.BillList
import com.github.sagiri_kawaii01.blenh.ui.component.SearchBar
import com.github.sagiri_kawaii01.blenh.ui.component.TimePeriodButton
import com.github.sagiri_kawaii01.blenh.ui.screen.dashboard.DashboardIntent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillListScreen(
    viewModel: BillListViewModel = hiltViewModel()
) {
    val uiState by viewModel.viewState.collectAsState()
    val dispatcher = viewModel.getDispatcher(startWith = BillListIntent.Init)

    when (uiState.billListDataState) {
        is BillListDataState.Success -> {
            val data = uiState.billListDataState as BillListDataState.Success

            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                topBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            TimePeriodButton("周", data.type == TimePeriodType.Week) {
                                dispatcher(BillListIntent.GetBillList(TimePeriodType.Week))
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            TimePeriodButton("月", data.type == TimePeriodType.Month) {
                                dispatcher(BillListIntent.GetBillList(TimePeriodType.Month))
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            TimePeriodButton("年", data.type == TimePeriodType.Year) {
                                dispatcher(BillListIntent.GetBillList(TimePeriodType.Year))
                            }
                        }
                        SearchBar {
                            dispatcher.invoke(BillListIntent.GetBillList(data.type, it))
                        }
                    }
                },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    BillList(
                        label = "消费记录",
                        billList = data.billList,
                        modifier = Modifier.background(Color.White)
                    )

                }
            }
        }
        is BillListDataState.Init -> {

        }
    }
}