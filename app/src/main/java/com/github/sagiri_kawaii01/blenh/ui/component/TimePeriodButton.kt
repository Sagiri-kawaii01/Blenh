package com.github.sagiri_kawaii01.blenh.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.sagiri_kawaii01.blenh.ui.theme.Blue20
import com.github.sagiri_kawaii01.blenh.ui.theme.Gray60

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun TimePeriodButton(
    label: String,
    active: Boolean,
    onClick: () -> Unit) {
    Text(
        text = label,
        color = if (active) Color.White else Color.Black,
        fontSize = 14.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .background(
                color = if (active) Blue20 else Gray60,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(vertical = 6.dp, horizontal = 14.dp)

            .clickable(indication = null, interactionSource = MutableInteractionSource()) {
                onClick()
            }

    )
}