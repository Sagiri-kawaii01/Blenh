package com.github.sagiri_kawaii01.blenh.ui.screen.edit

internal sealed interface EditStateChange {
    fun reduce(oldState: EditState): EditState
}