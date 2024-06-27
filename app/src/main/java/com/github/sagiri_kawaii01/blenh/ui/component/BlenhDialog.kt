package com.github.sagiri_kawaii01.blenh.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.github.sagiri_kawaii01.blenh.R

@Composable
fun BlenhDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    properties: DialogProperties = DialogProperties(),
    onDismissRequest: () -> Unit = {},
    icon: @Composable (() -> Unit)? = {
        val res = remember {
            arrayOf(
                R.raw.lottie_polite_chicky
            )
        }
        BlenhLottieAnimation(
            modifier = Modifier.size(48.dp),
            resId = remember { res.random() }
        )
    },
    title: @Composable (() -> Unit)? = null,
    text: @Composable (() -> Unit)? = null,
    selectable: Boolean = true,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null,
) {
    if (visible) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = confirmButton,
            dismissButton = dismissButton,
            properties = properties,
            modifier = modifier,
            icon = icon,
            title = title,
            text = {
                if (selectable) {
                    SelectionContainer(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        text?.invoke()
                    }
                } else {
                    text?.invoke()
                }
            }
        )
    }
}