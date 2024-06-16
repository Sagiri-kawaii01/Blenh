package com.github.sagiri_kawaii01.blenh.ui.screen.bottomsheet

import com.github.sagiri_kawaii01.blenh.model.bean.CategoryBean


internal sealed interface BottomSheetStateChange {
    fun reduce(oldState: BottomSheetState): BottomSheetState

    data object LoadingDialog : BottomSheetStateChange {
        override fun reduce(oldState: BottomSheetState): BottomSheetState = oldState.copy(loadingDialog = true)
    }

    sealed interface SheetData: BottomSheetStateChange {
        override fun reduce(oldState: BottomSheetState): BottomSheetState {
            return when (this) {
                Loading -> oldState.copy(loadingDialog = true)
                is BackCategory -> oldState.copy(
                    dataState = (oldState.dataState as BottomSheetDataState.Success).copy(
                        selectingCategory = true,
                        selectingType = false,
                        typePage = 0,
                        iconItems = iconItems
                    )
                )
                is Init -> {
                    oldState.copy(
                        dataState = BottomSheetDataState.Success(
                            selectingCategory = true,
                            selectingType = false,
                            selectedTypeId = selectTypeId,
                            selectedCategoryId = selectedCategoryId,
                            iconItems = iconItems
                        ).apply {
                            loading = false
                        }
                    )
                }
                is GetType -> {
                    oldState.copy(
                        dataState = (oldState.dataState as BottomSheetDataState.Success).copy(
                            selectingType = true,
                            selectingCategory = false,
                            iconItems = iconItems,
                            selectedCategoryId = categoryId
                        )
                    )
                }
                is CategoryPage -> {
                    oldState.copy(
                        dataState = (oldState.dataState as BottomSheetDataState.Success).copy(
                            categoryPage = page
                        )
                    )
                }
                is TypePage -> {
                    oldState.copy(
                        dataState = (oldState.dataState as BottomSheetDataState.Success).copy(
                            typePage = page
                        )
                    )
                }
                is SelectType -> {
                    oldState.copy(
                        dataState = (oldState.dataState as BottomSheetDataState.Success).copy(
                            selectedTypeId = typeId
                        )
                    )
                }
            }
        }

        data class GetType(
            val iconItems: List<Triple<String, Int, Int>> = emptyList(),
            val categoryId: Int
        ): SheetData

        data class Init(
            val selectedCategoryId: Int,
            val iconItems: List<Triple<String, Int, Int>>,
            val selectTypeId: Int,
        ): SheetData

        data class CategoryPage(val page: Int): SheetData
        data class TypePage(val page: Int): SheetData
        data class SelectType(val typeId: Int): SheetData
        data class BackCategory(val iconItems: List<Triple<String, Int, Int>>): SheetData

        data object Loading: SheetData
    }


}