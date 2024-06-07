package com.github.sagiri_kawaii01.blenh.base.mvi

import androidx.annotation.MainThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MviViewModel<I : MviIntent, S : MviViewState, E : MviSingleEvent> {
    val viewState: StateFlow<S>
    val singleEvent: Flow<E>
    @MainThread
    suspend fun processIntent(intent: I)
}