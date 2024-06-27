package com.github.sagiri_kawaii01.blenh.ui.screen.edit

import com.github.sagiri_kawaii01.blenh.base.mvi.MviIntent
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean

sealed interface EditIntent: MviIntent {

    data class Init(
        val billId: Int?
    ): EditIntent

    data class Save(
        val billBean: BillBean
    ): EditIntent

    data class Update(
        val billBean: BillBean
    ): EditIntent
}