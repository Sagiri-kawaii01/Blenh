package com.github.sagiri_kawaii01.blenh.ui.screen.billlist

import androidx.lifecycle.viewModelScope
import com.github.sagiri_kawaii01.blenh.base.mvi.AbstractMviViewModel
import com.github.sagiri_kawaii01.blenh.model.TimePeriodType
import com.github.sagiri_kawaii01.blenh.model.repository.BillRepository
import com.github.sagiri_kawaii01.blenh.model.repository.CategoryRepository
import com.github.sagiri_kawaii01.blenh.model.repository.IconRepository
import com.github.sagiri_kawaii01.blenh.model.repository.TypeRepository
import com.github.sagiri_kawaii01.blenh.util.formatDate
import com.github.sagiri_kawaii01.blenh.util.startWith
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillListViewModel @Inject constructor(
    private val billRepository: BillRepository,
    private val typeRepository: TypeRepository,
    private val categoryRepository: CategoryRepository,
    private val iconRepository: IconRepository,
): AbstractMviViewModel<BillListIntent, BillListState, BillListEvent>() {
    override val viewState: StateFlow<BillListState>
    private lateinit var typeNameMap: Map<Int, String>

    init {

        viewModelScope.launch(Dispatchers.IO) {
            typeNameMap = typeRepository.getTypeList().associate { it.id to it.name }
        }

        val initialVs = BillListState.initial()
        viewState = merge(
            intentSharedFlow.filterIsInstance<BillListIntent.Init>().take(1),
            intentSharedFlow.filterNot { it is BillListIntent.Init }
        )
            .shareWhileSubscribed()
            .debugLog("BillList")
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
    private fun SharedFlow<BillListIntent>.toPartialStateChangeFlow(): Flow<BillListStateChange> {
        return merge(
            filterIsInstance<BillListIntent.GetBillList>()
                .flatMapLatest { intent ->
                    combine (
                        billRepository.listSearch(intent.type, intent.search),
                        typeRepository.getTypeFlow(),
                        categoryRepository.getCategoryList(),
                        iconRepository.list()
                    ) { bills, types, categories, icons ->
                        val typeIdToIconIdx = kotlin.run {
                            val iconIdToIdx = icons.associate { it.id to it.resId }
                            val categoryToIcon = categories.associate { it.id to it.iconId }
                            types.associate { it.id to iconIdToIdx[categoryToIcon[it.categoryId]]!! }
                        }
                        BillListStateChange.BillList.Success(
                            type = intent.type,
                            billList = bills.map {
                                BillRecord(
                                    id = it.id,
                                    date = formatDate(it.month, it.day),
                                    time = it.time,
                                    money = it.money,
                                    label = if (it.remark != null) {
                                        it.remark!!
                                    } else {
                                        typeNameMap[it.typeId]!!
                                    },
                                    iconIdx = typeIdToIconIdx[it.typeId]!!,
                                    payType = it.payType
                                )
                            }
                        )
                    }
                }.startWith(BillListStateChange.BillList.Loading),

            filterIsInstance<BillListIntent.DeleteBill>()
                .flatMapLatest { intent ->
                    viewModelScope.launch(Dispatchers.IO) {
                        billRepository.deleteById(intent.id)
                    }
                    emptyFlow()
                },

            filterIsInstance<BillListIntent.Init>()
                .flatMapLatest {
                    processIntent(BillListIntent.GetBillList(TimePeriodType.Week))
                    emptyFlow()
                }
        )
    }
}