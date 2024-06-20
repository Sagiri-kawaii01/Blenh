package com.github.sagiri_kawaii01.blenh.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sagiri_kawaii01.blenh.ui.theme.BlenhTheme
import com.github.sagiri_kawaii01.blenh.ui.theme.Gray0
import com.github.sagiri_kawaii01.blenh.ui.theme.Gray20
import com.github.sagiri_kawaii01.blenh.ui.theme.Typography

@Composable
fun DashCard(
    label: String,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Card(
        border = BorderStroke(
            1.dp,
            Gray0
        )
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                style = Typography.labelMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            content()
        }
    }
}

