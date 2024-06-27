package com.github.sagiri_kawaii01.blenh.ui.component

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun BlenhLottieAnimation(
    modifier: Modifier = Modifier,
    @RawRes resId: Int,
    contentScale: ContentScale = ContentScale.Inside
) {
    val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val lottieProgress by animateLottieCompositionAsState(
        composition = lottieComposition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        modifier = modifier,
        composition = lottieComposition,
        progress = { lottieProgress },
        contentScale = contentScale,
    )
}