package com.github.sagiri_kawaii01.blenh.ui.screen.bottomsheet

import com.github.sagiri_kawaii01.blenh.base.mvi.MviIntent
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean

interface BottomSheetIntent: MviIntent {
    object GetCategories: BottomSheetIntent
    object BackCategory: BottomSheetIntent
    data class CategoryPage(val page: Int): BottomSheetIntent
    data class TypePage(val page: Int): BottomSheetIntent
    data class GetTypes(val categoryId: Int, val icon: Int): BottomSheetIntent
    data class SelectType(val typeId: Int): BottomSheetIntent
    data class Save(val bill: BillBean): BottomSheetIntent
}