package com.github.sagiri_kawaii01.blenh.ui.screen.dashboard

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.github.sagiri_kawaii01.blenh.base.mvi.AbstractMviViewModel
import com.github.sagiri_kawaii01.blenh.base.mvi.MviSingleEvent
import com.github.sagiri_kawaii01.blenh.model.TimePeriodType
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.model.db.repository.BillRepository
import com.github.sagiri_kawaii01.blenh.util.startWith
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val billRepository: BillRepository
): AbstractMviViewModel<DashboardIntent, DashboardState, MviSingleEvent>() {
    override val viewState: StateFlow<DashboardState>

    init {
        val initialVs = DashboardState.initial()
        viewState = merge(
            intentSharedFlow.filterIsInstance<DashboardIntent.GetBillList>().take(1),
            intentSharedFlow.filterNot { it is DashboardIntent.GetBillList }
        )
            .debugLog("Dashboard")
            .shareWhileSubscribed()
            .toPartialStateChangeFlow()
            .scan(initialVs) { vs, change ->
                change.reduce(vs)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                initialVs
            )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun SharedFlow<DashboardIntent>.toPartialStateChangeFlow(): Flow<DashboardStateChange> {
        return merge(
            filterIsInstance<DashboardIntent.GetBillList>()
        ).flatMapLatest<DashboardIntent.GetBillList, DashboardStateChange> {
            billRepository.list(it.type).transform { bills ->
                emit(
                    DashboardStateChange.BillList.Success(
                        it.type,
                        0.0,
                        0.0,
                        bills,
                        emptyList()
                    )
                )
            }
        }.startWith(DashboardStateChange.BillList.Loading)
    }


}