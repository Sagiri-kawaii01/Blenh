package com.github.sagiri_kawaii01.blenh.ui.screen.about.update

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.os.Build
import android.os.Handler
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat.getSystemService
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.sagiri_kawaii01.blenh.base.mvi.getDispatcher
import com.github.sagiri_kawaii01.blenh.ui.component.BlenhDialog
import com.github.sagiri_kawaii01.blenh.ui.component.WaitingDialog
import kotlinx.coroutines.flow.collect

@Composable
fun UpdateDialog(
    viewModel: UpdateViewModel = hiltViewModel(),
    onClose: () -> Unit
) {
    val uiState = viewModel.viewState.collectAsState()
    val dispatcher = viewModel.getDispatcher(startWith = UpdateIntent.CheckUpdate(isRetry = false))
    val uiEvent = viewModel.singleEvent.collectAsState(initial = null)
    var noticeVisible by remember { mutableStateOf(true) }

    WaitingDialog(
        visible = uiState.value.loadingDialog,
        title = "检查更新中"
    )

    when (uiState.value.updateUiState) {
        is UpdateUiState.OpenNewerDialog -> {
            val data = uiState.value.updateUiState as UpdateUiState.OpenNewerDialog
            BlenhDialog(
                visible = noticeVisible,
                confirmButton = {
                    Button(onClick = {
                        noticeVisible = false
                        dispatcher.invoke(
                            UpdateIntent.Update(
                                data.data.assets
                                    .find { it.browserDownloadUrl.contains(Build.SUPPORTED_ABIS[0]) }!!
                                    .browserDownloadUrl
                            )
                        )
                    }) {
                        Text(text = "更新")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        noticeVisible = false
                        onClose()
                    }) {
                        Text(text = "取消")
                    }
                },
                title = {
                    Text(text = "发现新版本: ${data.data.name}_${data.data.publishedAt}")
                },
                text = {
                    Text(text = data.data.body)
                }
            )
        }
        is UpdateUiState.NetworkError -> {
            BlenhDialog(
                visible = noticeVisible,
                confirmButton = {
                    Button(onClick = {
                        dispatcher.invoke(UpdateIntent.CheckUpdate(isRetry = true))
                    }) {
                        Text(text = "重试")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        noticeVisible = false
                        onClose()
                    }) {
                        Text(text = "取消")
                    }
                },
                title = {
                    Text(text = "网络错误")
                },
                text = {
                    Text(text = "请检查是否可以访问Github")
                }
            )
        }

        is UpdateUiState.Downloading -> {
            val data = uiState.value.updateUiState as UpdateUiState.Downloading
            WaitingDialog(
                visible = true,
                title = "下载中",
                totalValue = 100,
                currentValue = data.progress
            )
        }
        else -> {}
    }

}
