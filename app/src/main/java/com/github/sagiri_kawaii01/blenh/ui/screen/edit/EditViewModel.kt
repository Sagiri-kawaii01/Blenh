package com.github.sagiri_kawaii01.blenh.ui.screen.edit

import androidx.lifecycle.viewModelScope
import com.github.sagiri_kawaii01.blenh.base.mvi.AbstractMviViewModel
import com.github.sagiri_kawaii01.blenh.base.mvi.MviSingleEvent
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.model.db.repository.BillRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val billRepository: BillRepository
): AbstractMviViewModel<EditIntent, EditState, EditEvent>() {
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

    suspend fun getBill(billId: Int): BillBean {
        return billRepository.getById(billId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun SharedFlow<EditIntent>.toPartialStateChangeFlow(): Flow<EditStateChange> {
        return merge(
            filterIsInstance<EditIntent.Save>()
                .flatMapLatest {
                    viewModelScope.launch(Dispatchers.IO) {
                        billRepository.insert(it.billBean)
                        withContext(Dispatchers.Main.immediate) {
                            sendEvent(EditEvent.SaveSuccess)
                        }
                    }
                    flow {
                        emit(EditStateChange.Saving)
                    }
                },

            filterIsInstance<EditIntent.Update>()
                .flatMapLatest {
                    viewModelScope.launch(Dispatchers.IO) {
                        billRepository.update(it.billBean)
                        withContext(Dispatchers.Main.immediate) {
                            sendEvent(EditEvent.SaveSuccess)
                        }
                    }
                    flow {
                        emit(EditStateChange.Saving)
                    }
                }
        )
    }
}