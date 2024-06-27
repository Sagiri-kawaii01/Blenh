package com.github.sagiri_kawaii01.blenh.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.sagiri_kawaii01.blenh.R

@Composable
fun WaitingDialog(
    visible: Boolean,
    currentValue: Int? = null,
    totalValue: Int? = null,
    msg: String? = null,
    title: String = "加载中"
) {
    if (currentValue == null || totalValue == null) {
        WaitingDialog(
            visible = visible,
            title = title,
            text = if (msg == null) null else {
                {
                    Text(
                        text = msg,
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        )
    } else {
        WaitingDialog(visible = visible, title = title) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val animatedProgress by animateFloatAsState(
                    targetValue = currentValue.toFloat() / totalValue,
                    animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
                    label = "waitingDialogAnimatedProgress"
                )
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.semantics(mergeDescendants = true) {},
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "$currentValue / $totalValue",
                    style = MaterialTheme.typography.labelLarge
                )
                if (msg != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = msg,
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
fun WaitingDialog(
    visible: Boolean,
    title: String = "加载中",
    text: @Composable (() -> Unit)? = null
) {
    BlenhDialog(
        visible = visible, 
        confirmButton = {},
        onDismissRequest = {},
        icon = {
            BlenhLottieAnimation(
                resId = R.raw.lottie_polite_chicky,
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(text = title)
        },
        text = text
    )
}