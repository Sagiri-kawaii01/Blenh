package com.github.sagiri_kawaii01.blenh.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState


@Composable
fun <T> DeleteConfirmDialog(
    data: T,
    text: String,
    onDismiss: () -> Unit,
    onConfirm: (T) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "确认删除")
        },
        text = {
            Column {
                Text(text = "你确定要删除这条记录吗？")
                Text(text = text)
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(data)
                onDismiss()
            }) {
                Text(text = "确认")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "取消")
            }
        }
    )
}