package com.github.sagiri_kawaii01.blenh.ui.local

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalNavController = compositionLocalOf<NavHostController> {
    error("LocalNavController not initialized!")
}