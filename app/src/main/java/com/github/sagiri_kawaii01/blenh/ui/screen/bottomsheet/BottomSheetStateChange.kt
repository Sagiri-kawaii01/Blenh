package com.github.sagiri_kawaii01.blenh.ui.screen.bottomsheet


internal sealed interface BottomSheetStateChange {
    fun reduce(oldState: BottomSheetState): BottomSheetState

    data object LoadingDialog : BottomSheetStateChange {
        override fun reduce(oldState: BottomSheetState): BottomSheetState = oldState.copy(loadingDialog = true)
    }

    sealed interface SheetData: BottomSheetStateChange {
        override fun reduce(oldState: BottomSheetState): BottomSheetState {
            return when (this) {
                Loading -> oldState.copy(loadingDialog = true)
                BackCategory -> oldState.copy(
                    dataState = (oldState.dataState as BottomSheetDataState.Success).copy(
                        selectingCategory = true,
                        selectingType = false,
                        typePage = 0
                    )
                )
                is Init -> {
                    oldState.copy(
                        dataState = BottomSheetDataState.Success(
                            selectingCategory = true,
                            selectingType = false,
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
                            iconItems = iconItems
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
            }
        }

        data class GetType(
            val iconItems: List<Triple<String, Int, Int>> = emptyList()
        ): SheetData

        data class Init(
            val selectedCategoryId: Int,
            val iconItems: List<Triple<String, Int, Int>>
        ): SheetData

        data class CategoryPage(val page: Int): SheetData
        data class TypePage(val page: Int): SheetData
        data object BackCategory: SheetData

        data object Loading: SheetData
    }


}