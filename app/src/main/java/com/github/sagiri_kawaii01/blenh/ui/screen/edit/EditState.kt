package com.github.sagiri_kawaii01.blenh.ui.screen.edit

import com.github.sagiri_kawaii01.blenh.base.mvi.MviViewState
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.model.bean.CategoryBean
import com.github.sagiri_kawaii01.blenh.model.bean.TypeBean

data class EditState(
    val loadingDialog: Boolean,
    val savingDialog: Boolean,
    val dataState: EditDataState
): MviViewState {
    companion object {
        fun initial() = EditState(
            loadingDialog = false,
            savingDialog = false,
            dataState = EditDataState.Init
        )
    }

    sealed class EditDataState {
        var loading: Boolean = false

        data class CategoryListSuccess(
            val categoryList: List<CategoryBean>,
            val typeList: List<TypeBean>,
            val selectedCategoryId: Int,
            val selectedTypeId: Int,
            val editBill: BillBean? = null
        ): EditDataState()

        data object Init: EditDataState()
    }
}