package com.github.sagiri_kawaii01.blenh.base.mvi

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sagiri_kawaii01.blenh.model.bean.IconBean
import com.github.sagiri_kawaii01.blenh.model.db.repository.IconRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val iconRepository: IconRepository
) : ViewModel() {
    private val _icons: MutableState<List<IconBean>> = mutableStateOf(emptyList())
    val icons: State<List<IconBean>> = _icons

    fun getIcons() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _icons.value = iconRepository.list()
            } catch (e: Exception) {
                Log.e("TestViewModel", "getIcons: ${e.message}")
            }
        }
    }
}