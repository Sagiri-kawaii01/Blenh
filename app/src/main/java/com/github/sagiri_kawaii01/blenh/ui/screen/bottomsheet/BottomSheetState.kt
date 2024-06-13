package com.github.sagiri_kawaii01.blenh.ui.screen.bottomsheet

import com.github.sagiri_kawaii01.blenh.base.mvi.MviViewState

data class BottomSheetState(
    val loadingDialog: Boolean,
    val dataState: BottomSheetDataState
): MviViewState {
    companion object {
        fun initial() = BottomSheetState(
            loadingDialog = false,
            dataState = BottomSheetDataState.Init
        )
    }
}

sealed class BottomSheetDataState {
    var loading: Boolean = false

    data object Init: BottomSheetDataState()
    data class Success(
        val selectingCategory: Boolean,
        val selectingType: Boolean,
        val categoryPage: Int = 0,
        val typePage: Int = 0,
        val selectedCategoryId: Int? = null,
        val iconItems: List<Triple<String, Int, Int>> = emptyList()
    ): BottomSheetDataState()
}

