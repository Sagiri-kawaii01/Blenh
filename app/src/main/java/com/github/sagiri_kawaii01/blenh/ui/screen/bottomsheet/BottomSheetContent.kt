package com.github.sagiri_kawaii01.blenh.ui.screen.bottomsheet

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.github.sagiri_kawaii01.blenh.base.mvi.getDispatcher
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.model.bean.IconBean
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SheetContent(
    bill: BillBean,
    viewModel: BottomSheetViewModel,
    pageSize: Int = 8,
) {

    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val uiState by viewModel.viewState.collectAsState()
        val dispatcher = viewModel.getDispatcher(startWith = BottomSheetIntent.GetCategories)
        when (uiState.dataState) {
            is BottomSheetDataState.Init -> {}
            is BottomSheetDataState.Success -> {
                val data = (uiState.dataState as BottomSheetDataState.Success)
                val pagerState = rememberPagerState(pageCount = {
                    (pageSize - 1 + data.iconItems.size) / pageSize
                })

                HorizontalPager(state = pagerState) { page ->
                    val offset = pageSize * page
                    val pageDataCount = (data.iconItems.size - offset).let {
                        if (it > pageSize) {
                            pageSize
                        } else {
                            it
                        }
                    }
                    val line1Size = if (pageDataCount > pageSize / 2) {
                        pageSize / 2
                    } else {
                        pageDataCount
                    }
                    val lin2Size = pageDataCount - line1Size
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for (i in 0 until line1Size) {
                            IconItem(
                                icon = IconBean.IconList[data.iconItems[offset + i].third],
                                text = data.iconItems[offset + i].first
                            ) {
                                dispatcher.invoke(BottomSheetIntent.GetTypes(data.iconItems[offset + i].second, data.iconItems[offset + i].third))
                            }
                        }
                    }
                }

            }
        }


    }
}

@Composable
fun IconItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.size(48.dp)
            .clickable {
                onClick()
            }
    ) {
        Icon(imageVector = icon, contentDescription = text)
        Text(text = text)
    }
}

