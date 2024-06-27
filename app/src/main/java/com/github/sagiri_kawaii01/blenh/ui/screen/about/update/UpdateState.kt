package com.github.sagiri_kawaii01.blenh.ui.screen.about.update

import UpdateBean
import com.github.sagiri_kawaii01.blenh.base.mvi.MviViewState

data class UpdateState(
    var updateUiState: UpdateUiState,
    var loadingDialog: Boolean,
) : MviViewState {
    companion object {
        fun initial() = UpdateState(
            updateUiState = UpdateUiState.Init,
            loadingDialog = false,
        )
    }
}

sealed class UpdateUiState {
    data class OpenNewerDialog(val data: UpdateBean) : UpdateUiState()
    data object OpenNoUpdateDialog : UpdateUiState()
    data object Init : UpdateUiState()
}