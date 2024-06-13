package com.github.sagiri_kawaii01.blenh.ui.screen.bottomsheet

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.ui.screen.dashboard.DashboardViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    bill: BillBean,
    createViewModel: () -> BottomSheetViewModel
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val viewModel: BottomSheetViewModel = remember {
        createViewModel()
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            SheetContent(bill, viewModel)
        },
        sheetPeekHeight = 0.dp,
        modifier = Modifier.wrapContentSize(),
    ) {

    }

    LaunchedEffect(Unit) {
        scaffoldState.bottomSheetState.expand()
    }
}

@Composable
fun SheetContent(
    bill: BillBean,
    viewModel: BottomSheetViewModel
) {

    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = bill.time)
        Text(text = bill.payMethod ?: "unknown method")
        Button(onClick = {
            Log.d("SheetContent", bill.toString())
        }) {
            Text(text = "按钮")
        }
    }
}