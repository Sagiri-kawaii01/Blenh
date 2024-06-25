package com.github.sagiri_kawaii01.blenh.ui.screen.edit

import com.github.sagiri_kawaii01.blenh.base.mvi.MviViewState

data class EditState(
    val loadingDialog: Boolean,
    val savingDialog: Boolean,
): MviViewState {
    companion object {
        fun initial() = EditState(
            loadingDialog = false,
            savingDialog = false
        )
    }
}