package com.github.sagiri_kawaii01.blenh.ui.screen.edit

import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.model.bean.CategoryBean
import com.github.sagiri_kawaii01.blenh.model.bean.TypeBean

internal sealed interface EditStateChange {
    fun reduce(oldState: EditState): EditState

    sealed interface DialogStateChange: EditStateChange {

        data object SavingDialog: DialogStateChange
        data object SavingSuccess: DialogStateChange
        data object LoadingDialog: DialogStateChange

        override fun reduce(oldState: EditState): EditState {
            return when (this) {
                SavingDialog -> oldState.copy(savingDialog = true)
                SavingSuccess -> oldState.copy(savingDialog = false)
                LoadingDialog -> oldState.copy(loadingDialog = true)
            }
        }
    }

    data class TypeListSuccess(
        val typeList: List<TypeBean>
    ): EditStateChange {
        override fun reduce(oldState: EditState): EditState {
            return oldState.copy(
                dataState = (oldState.dataState as EditState.EditDataState.CategoryListSuccess).copy(
                    typeList = typeList,
                    selectedTypeId = typeList[0].id
                ),
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
                ),
                loadingDialog = false
            )
        }
    }
}