package com.github.sagiri_kawaii01.blenh.ui.screen.bottomsheet

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sagiri_kawaii01.blenh.model.db.repository.BillRepository
import com.github.sagiri_kawaii01.blenh.model.db.repository.TypeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BottomSheetViewModel(
    private val billRepository: BillRepository,
    private val typeRepository: TypeRepository
): ViewModel() {
    lateinit var typeNameMap: Map<Int, String>

    init {
        viewModelScope.launch(Dispatchers.IO) {
            typeNameMap = typeRepository.getTypeList().associate { it.id to it.name }
            typeNameMap.forEach { t, u ->
                Log.d("BottomSheetViewModel", "$t -> $u")
            }
        }
    }
}