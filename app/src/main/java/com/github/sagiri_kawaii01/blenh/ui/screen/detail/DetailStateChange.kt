package com.github.sagiri_kawaii01.blenh.ui.screen.detail

import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.model.bean.CategoryBean
import com.github.sagiri_kawaii01.blenh.model.bean.IconBean
import com.github.sagiri_kawaii01.blenh.model.bean.TypeBean

internal sealed interface DetailStateChange {
    fun reduce(oldState: DetailState): DetailState

    data object LoadingDialog : DetailStateChange {
        override fun reduce(oldState: DetailState): DetailState = oldState.copy(loadingDialog = true)
    }

    sealed interface BillDetail: DetailStateChange {
        override fun reduce(oldState: DetailState): DetailState {
            return when (this) {
                Loading -> oldState.copy(
                    detailDataState = oldState.detailDataState.apply { loading = true }
                )

                is Success -> oldState.copy(
                    detailDataState = DetailDataState.Success(
                        bill = bill,
                        type = type,
                        category = category,
                        icon = icon
                    ).apply {
                        loading = false
                    }
                )
            }
        }

        data class Success(
            val bill: BillBean,
            val type: TypeBean,
            val category: CategoryBean,
            val icon: IconBean
        ): BillDetail

        data object Loading: BillDetail
    }
}