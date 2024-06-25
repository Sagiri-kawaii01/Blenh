package com.github.sagiri_kawaii01.blenh.ui.screen.edit

internal sealed interface EditStateChange {
    fun reduce(oldState: EditState): EditState

    data object Saving: EditStateChange {
        override fun reduce(oldState: EditState): EditState {
            return oldState.copy(
                savingDialog = true
            )
        }
    }
}