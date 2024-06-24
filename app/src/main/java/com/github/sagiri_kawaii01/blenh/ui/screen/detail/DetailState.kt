package com.github.sagiri_kawaii01.blenh.ui.screen.detail

import com.github.sagiri_kawaii01.blenh.base.mvi.MviViewState
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.model.bean.CategoryBean
import com.github.sagiri_kawaii01.blenh.model.bean.IconBean
import com.github.sagiri_kawaii01.blenh.model.bean.TypeBean

data class DetailState(
    val loadingDialog: Boolean,
    val detailDataState: DetailDataState
): MviViewState {
    companion object {
        fun initial() = DetailState(
            loadingDialog = false,
            detailDataState = DetailDataState.Init
        )
    }
}

sealed class DetailDataState {
    var loading: Boolean = false

    data object Init: DetailDataState()
    data class Success(
        val bill: BillBean,
        val type: TypeBean,
        val category: CategoryBean,
        val icon: IconBean
    ): DetailDataState()
}