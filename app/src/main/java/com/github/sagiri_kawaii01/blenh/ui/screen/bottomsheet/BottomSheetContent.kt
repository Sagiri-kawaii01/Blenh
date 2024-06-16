package com.github.sagiri_kawaii01.blenh.ui.screen.bottomsheet

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.github.sagiri_kawaii01.blenh.base.mvi.MviSingleEvent
import com.github.sagiri_kawaii01.blenh.base.mvi.getDispatcher
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.model.bean.IconBean
import com.github.sagiri_kawaii01.blenh.model.bean.TypeBean
import com.github.sagiri_kawaii01.blenh.ui.screen.dashboard.DashboardViewModel
import com.github.sagiri_kawaii01.blenh.ui.theme.Gray20
import com.github.sagiri_kawaii01.blenh.ui.theme.Gray60
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    bill: BillBean,
    onDismissRequest: () -> Unit,
    createViewModel: () -> BottomSheetViewModel,
) {

    val viewModel: BottomSheetViewModel = remember {
        createViewModel()
    }

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        modifier = Modifier.wrapContentSize(),
    ) {
        SheetContent(bill, viewModel, onCancel = onDismissRequest)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SheetContent(
    bill: BillBean,
    viewModel: BottomSheetViewModel,
    pageSize: Int = 6,
    onCancel: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val uiState by viewModel.viewState.collectAsState()
        val dispatcher = viewModel.getDispatcher(startWith = BottomSheetIntent.GetCategories)
        val uiEvent by viewModel.singleEvent.collectAsState(initial = null)

        when (uiEvent) {
            is BottomSheetEvent.SaveSuccess -> onCancel()
        }

        SheetTypeSelector(
            uiState = uiState,
            dispatcher = dispatcher,
            pageSize = pageSize
        )
        
        HorizontalDivider()
        
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.height(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "￥${bill.money}",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Right
            )

            Spacer(modifier = Modifier.width(32.dp))

            Text(
                text = "备注",
                modifier = Modifier.weight(1f)
            )

        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(
                modifier = Modifier
                    .width(109.dp)
                    .height(48.dp),
                border = BorderStroke(1.dp, Gray20),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors().copy(containerColor = Color.White),
                onClick = onCancel
            ) {
                Text(
                    text = "取消",
                    color = Color.Black
                )
            }

            Button(
                modifier = Modifier
                    .width(109.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors().copy(containerColor = Color.Black.copy(0.75f)),
                onClick = {
                    val data = uiState.dataState as BottomSheetDataState.Success
                    bill.typeId = data.selectedTypeId ?: 0
                    dispatcher.invoke(BottomSheetIntent.Save(bill))
                    // todo 保存成功event触发关闭窗口
                }
            ) {
                Text(
                    text = "保存",
                    color = Color.White
                )
            }
        }
        
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SheetTypeSelector(
    uiState: BottomSheetState,
    dispatcher: (BottomSheetIntent) -> Unit,
    pageSize: Int
) {
    when (uiState.dataState) {
        is BottomSheetDataState.Init -> {}
        is BottomSheetDataState.Success -> {
            val data = (uiState.dataState as BottomSheetDataState.Success)
            val pagerState = rememberPagerState(pageCount = {
                (pageSize - 1 + data.iconItems.size) / pageSize
            })

            LaunchedEffect(uiState.dataState.selectingCategory) {
                if (uiState.dataState.selectingCategory) {
                    pagerState.animateScrollToPage(uiState.dataState.categoryPage)
                }
            }

            LaunchedEffect(pagerState.currentPage) {
                if (uiState.dataState.selectingCategory) {
                    dispatcher.invoke(BottomSheetIntent.CategoryPage(pagerState.currentPage))
                } else {
                    dispatcher.invoke(BottomSheetIntent.TypePage(pagerState.currentPage))
                }
            }


            Column {
                if (uiState.dataState.selectingType) {
                    IconButton(
                        onClick = {
                            dispatcher.invoke(BottomSheetIntent.BackCategory)
                        },
                        modifier = Modifier.height(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBackIosNew,
                            contentDescription = "backCategory"
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(12.dp))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(144.dp),
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "<",
                            color = if (pagerState.currentPage == 0) {
                                Gray60
                            } else {
                                Gray20
                            }
                        )
                    }
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.weight(1f)
                    ) { page ->
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
                        val line2Size = pageDataCount - line1Size
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                for (i in 0 until line1Size) {
                                    IconItem(
                                        icon = IconBean.IconList[data.iconItems[offset + i].third],
                                        text = data.iconItems[offset + i].first,
                                        select = if (data.selectingCategory) {
                                            data.selectedCategoryId == data.iconItems[offset + i].second
                                        } else {
                                            data.selectedTypeId == data.iconItems[offset + i].second
                                        }
                                    ) {
                                        if (data.selectingCategory) {
                                            dispatcher.invoke(BottomSheetIntent.GetTypes(data.iconItems[offset + i].second, data.iconItems[offset + i].third))
                                        } else {
                                            dispatcher.invoke(BottomSheetIntent.SelectType(data.iconItems[offset + i].second))
                                        }
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                for (i in 0 until line2Size) {
                                    val j = i + pageSize / 2
                                    IconItem(
                                        icon = IconBean.IconList[data.iconItems[offset + j].third],
                                        text = data.iconItems[offset + j].first,
                                        select = if (data.selectingCategory) {
                                            data.selectedCategoryId == data.iconItems[offset + j].second
                                        } else {
                                            data.selectedTypeId == data.iconItems[offset + j].second
                                        }
                                    ) {
                                        if (data.selectingCategory) {
                                            dispatcher.invoke(BottomSheetIntent.GetTypes(data.iconItems[offset + j].second, data.iconItems[offset + j].third))
                                        } else {
                                            dispatcher.invoke(BottomSheetIntent.SelectType(data.iconItems[offset + j].second))
                                        }
                                    }
                                }
                                if (0 == line2Size) {
                                    Spacer(modifier = Modifier.height(72.dp))
                                }
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = ">",
                            color = if (pagerState.currentPage == pagerState.pageCount - 1) {
                                Gray60
                            } else {
                                Gray20
                            }
                        )
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
    select: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .size(72.dp)
            .clickable {
                onClick()
            }
            .run {
                if (select) {
                    border(BorderStroke(1.dp, Gray20), shape = RoundedCornerShape(8.dp))
                } else this
            }
    ) {
        Icon(imageVector = icon, contentDescription = text)
        Text(text = text)
    }
}

