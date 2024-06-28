package com.github.sagiri_kawaii01.blenh.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.sagiri_kawaii01.blenh.ui.theme.Green60

@Composable
fun PermissionLine(
    text: String,
    enable: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text)

        if (enable) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Opened",
                tint = Green60
            )
        } else {
            Text(
                text = "前往开启 ->",
                modifier = Modifier.clickable {
                    onClick()
                }
            )
        }
    }
}