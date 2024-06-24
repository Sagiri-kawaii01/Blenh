package com.github.sagiri_kawaii01.blenh.ui.screen.detail

import androidx.lifecycle.viewModelScope
import com.github.sagiri_kawaii01.blenh.base.mvi.AbstractMviViewModel
import com.github.sagiri_kawaii01.blenh.base.mvi.MviSingleEvent
import com.github.sagiri_kawaii01.blenh.model.db.repository.BillRepository
import com.github.sagiri_kawaii01.blenh.model.db.repository.CategoryRepository
import com.github.sagiri_kawaii01.blenh.model.db.repository.IconRepository
import com.github.sagiri_kawaii01.blenh.model.db.repository.TypeRepository
import com.github.sagiri_kawaii01.blenh.util.flowOnIo
import com.github.sagiri_kawaii01.blenh.util.startWith
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val billRepository: BillRepository,
    private val typeRepository: TypeRepository,
    private val iconRepository: IconRepository,
    private val categoryRepository: CategoryRepository
): AbstractMviViewModel<DetailIntent, DetailState, MviSingleEvent>() {
    override val viewState: StateFlow<DetailState>

    init {
        val initialVs = DetailState.initial()

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

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun SharedFlow<DetailIntent>.toPartialStateChangeFlow(): Flow<DetailStateChange> {
        return filterIsInstance<DetailIntent.GetDetail>()
            .flatMapLatest { intent ->
                flow {
                    val bill = billRepository.getById(intent.billId)
                    val type = typeRepository.getById(bill.typeId)
                    val category = categoryRepository.getById(type.categoryId)
                    val icon = iconRepository.getById(category.iconId)
                    emit(DetailStateChange.BillDetail.Success(
                        bill = bill,
                        type = type,
                        category = category,
                        icon = icon
                    ))
                }.flowOnIo()
            }.startWith(DetailStateChange.LoadingDialog)
    }
}