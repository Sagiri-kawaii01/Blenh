package com.github.sagiri_kawaii01.blenh.ui.screen.dashboard

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.github.sagiri_kawaii01.blenh.base.mvi.AbstractMviViewModel
import com.github.sagiri_kawaii01.blenh.base.mvi.MviSingleEvent
import com.github.sagiri_kawaii01.blenh.model.TimePeriodType
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.model.repository.BillRepository
import com.github.sagiri_kawaii01.blenh.model.repository.TypeRepository
import com.github.sagiri_kawaii01.blenh.util.startWith
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val billRepository: BillRepository,
    private val typeRepository: TypeRepository,
): AbstractMviViewModel<DashboardIntent, DashboardState, MviSingleEvent>() {
    override val viewState: StateFlow<DashboardState>
    private lateinit var typeNameMap: Map<Int, String>

    init {
        viewModelScope.launch(Dispatchers.IO) {
            typeNameMap = typeRepository.getTypeList().associate { it.id to it.name }
        }

        val initialVs = DashboardState.initial()

        viewState = intentSharedFlow
            .shareWhileSubscribed()
            .debugLog("Dashboard")
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
                .flatMapLatest { intent ->
                    combine(
                        billRepository.list(intent.type),
                        billRepository.expend(intent.type),
                        billRepository.charts(intent.type)
                    ) { bills, expend, charts ->
                        delay(1000)
                        DashboardStateChange.BillList.Success(
                            intent.type,
                            expend,
                            0.0 to 0.0,
                            bills,
                            charts,
                            typeNameMap
                        )
                    }
                }.startWith(DashboardStateChange.LoadingDialog),

            filterIsInstance<DashboardIntent.Delete>()
                .flatMapLatest { intent ->
                    viewModelScope.launch(Dispatchers.IO) {
                        billRepository.deleteById(intent.id)
                    }
                    emptyFlow()
                }
        )
    }


}