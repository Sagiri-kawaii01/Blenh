package com.github.sagiri_kawaii01.blenh.ui.screen.edit

import androidx.lifecycle.viewModelScope
import com.github.sagiri_kawaii01.blenh.base.mvi.AbstractMviViewModel
import com.github.sagiri_kawaii01.blenh.model.repository.BillRepository
import com.github.sagiri_kawaii01.blenh.model.repository.CategoryRepository
import com.github.sagiri_kawaii01.blenh.model.repository.TypeRepository
import com.github.sagiri_kawaii01.blenh.util.flowOnIo
import com.github.sagiri_kawaii01.blenh.util.startWith
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
    private val billRepository: BillRepository,
    private val categoryRepository: CategoryRepository,
    private val typeRepository: TypeRepository
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun SharedFlow<EditIntent>.toPartialStateChangeFlow(): Flow<EditStateChange> {
        return merge(

            filterIsInstance<EditIntent.GetTypes>()
                .flatMapLatest {
                    flow<EditStateChange> {
                        emit(
                            EditStateChange.TypeListSuccess(
                                typeList = typeRepository.typeList(it.categoryId)
                            )
                        )
                    }.flowOnIo()
                },

            filterIsInstance<EditIntent.Save>()
                .flatMapLatest {
                    flow<EditStateChange> {
                        billRepository.insert(it.billBean)
                        emit(EditStateChange.DialogStateChange.SavingSuccess)
                        withContext(Dispatchers.Main.immediate) {
                            sendEvent(EditEvent.SaveSuccess)
                        }
                    }.flowOnIo()
                        .startWith(EditStateChange.DialogStateChange.SavingDialog)
                },

            filterIsInstance<EditIntent.Update>()
                .flatMapLatest {
                    flow {
                        billRepository.update(it.billBean)
                        emit(EditStateChange.DialogStateChange.SavingSuccess)
                        withContext(Dispatchers.Main.immediate) {
                            sendEvent(EditEvent.SaveSuccess)
                        }
                    }.flowOnIo()
                        .startWith(EditStateChange.DialogStateChange.SavingDialog)
                },

            filterIsInstance<EditIntent.Init>()
                .flatMapLatest { intent ->
                    flow {
                        val categories = categoryRepository.categoryList()
                        val bill = if (intent.billId == null) {
                            null
                        } else {
                            billRepository.getById(intent.billId)
                        }
                        val type = if (intent.billId == null) {
                            typeRepository.typeList(categories[0].id)
                        } else {
                            typeRepository.sameKindTypes(bill!!.typeId)
                        }
                        emit(EditStateChange.CategoryListSuccess(
                            categoryList = categories,
                            typeList = type,
                            selectedCategoryId = if (intent.billId == null) {
                                categories[0].id
                            } else {
                                type.find { it.id == bill!!.typeId }!!.categoryId
                            },
                            selectedTypeId = if (intent.billId == null) {
                                type[0].id
                            } else {
                                bill!!.typeId
                            },
                            editBill = bill
                        ))
                    }.flowOnIo()
                        .startWith(EditStateChange.DialogStateChange.LoadingDialog)
                }
        )
    }
}