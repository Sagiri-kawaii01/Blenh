package com.github.sagiri_kawaii01.blenh.ui.screen.edit

import androidx.lifecycle.viewModelScope
import com.github.sagiri_kawaii01.blenh.base.mvi.AbstractMviViewModel
import com.github.sagiri_kawaii01.blenh.base.mvi.MviSingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(

): AbstractMviViewModel<EditIntent, EditState, MviSingleEvent>() {
    override val viewState: StateFlow<EditState>

    init {
        val initialVs = EditState.initial()
        viewState = intentSharedFlow
            .toPartialStateChangeFlow()
            .scan(initialVs) { vs, change ->
                change.reduce(vs)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(),
                initialVs
            )
    }

    private fun SharedFlow<EditIntent>.toPartialStateChangeFlow(): Flow<EditStateChange> {
        return emptyFlow()
    }
}