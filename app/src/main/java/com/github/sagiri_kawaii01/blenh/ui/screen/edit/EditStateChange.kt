package com.github.sagiri_kawaii01.blenh.ui.screen.edit

import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.model.bean.CategoryBean
import com.github.sagiri_kawaii01.blenh.model.bean.TypeBean

internal sealed interface EditStateChange {
    fun reduce(oldState: EditState): EditState

    data object Saving: EditStateChange {
        override fun reduce(oldState: EditState): EditState {
            return oldState.copy(
                savingDialog = true
            )
        }
    }

    data class CategoryListSuccess(
        val categoryList: List<CategoryBean>,
        val typeList: List<TypeBean>,
        val selectedCategoryId: Int,
        val selectedTypeId: Int,
        val editBill: BillBean?
    ): EditStateChange {
        override fun reduce(oldState: EditState): EditState {
            return oldState.copy(
                dataState = EditState.EditDataState.CategoryListSuccess(
                    categoryList = categoryList,
                    typeList = typeList,
                    selectedCategoryId = selectedCategoryId,
                    selectedTypeId = selectedTypeId,
                    editBill = editBill
                )
            )
        }
    }
}