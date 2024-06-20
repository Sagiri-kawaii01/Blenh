package com.github.sagiri_kawaii01.blenh.ui.screen.billlist

import android.util.Log
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.sagiri_kawaii01.blenh.base.mvi.getDispatcher
import com.github.sagiri_kawaii01.blenh.model.TimePeriodType
import com.github.sagiri_kawaii01.blenh.ui.component.SearchBar
import com.github.sagiri_kawaii01.blenh.ui.component.TimePeriodButton
import com.github.sagiri_kawaii01.blenh.ui.screen.dashboard.DashboardIntent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillListScreen(
    viewModel: BillListViewModel = hiltViewModel()
) {
    val uiState by viewModel.viewState.collectAsState()
    val dispatcher = viewModel.getDispatcher(startWith = BillListIntent.GetBillList(type = TimePeriodType.Week))

    when (uiState.billListDataState) {
        is BillListDataState.Success -> {
            val data = uiState.billListDataState as BillListDataState.Success

            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                topBar = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
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
                            Log.d("Search", it)
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
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {




            }
        }
        is BillListDataState.Init -> {

        }
    }
}