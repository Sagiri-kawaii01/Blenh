package com.github.sagiri_kawaii01.blenh.ui.screen.bottomsheet

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sagiri_kawaii01.blenh.base.mvi.AbstractMviViewModel
import com.github.sagiri_kawaii01.blenh.base.mvi.MviSingleEvent
import com.github.sagiri_kawaii01.blenh.model.bean.CategoryBean
import com.github.sagiri_kawaii01.blenh.model.bean.IconBean
import com.github.sagiri_kawaii01.blenh.model.bean.TypeBean
import com.github.sagiri_kawaii01.blenh.model.db.repository.BillRepository
import com.github.sagiri_kawaii01.blenh.model.db.repository.CategoryRepository
import com.github.sagiri_kawaii01.blenh.model.db.repository.IconRepository
import com.github.sagiri_kawaii01.blenh.model.db.repository.TypeRepository
import com.github.sagiri_kawaii01.blenh.ui.screen.dashboard.DashboardIntent
import com.github.sagiri_kawaii01.blenh.ui.screen.dashboard.DashboardState
import com.github.sagiri_kawaii01.blenh.util.flowOnIo
import com.github.sagiri_kawaii01.blenh.util.startWith
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

class BottomSheetViewModel(
    private val billRepository: BillRepository,
    private val typeRepository: TypeRepository,
    private val iconRepository: IconRepository,
    private val categoryRepository: CategoryRepository
): AbstractMviViewModel<BottomSheetIntent, BottomSheetState, MviSingleEvent>() {
    override val viewState: StateFlow<BottomSheetState>

    private lateinit var categories: List<CategoryBean>
    private lateinit var iconMap: Map<Int, Int>

    init {
        val initialVs = BottomSheetState.initial()
        viewState = merge(
            intentSharedFlow.filterIsInstance<BottomSheetIntent.GetCategories>().take(1),
            intentSharedFlow.filterNot { it is BottomSheetIntent.GetCategories }
        )
            .shareWhileSubscribed()
            .debugLog("BottomSheet")
            .toPartialStateChangeFlow()
            .scan(initialVs) { vs, change ->
                Log.d("Reduce", change.toString())
                change.reduce(vs)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                initialVs
            )

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun SharedFlow<BottomSheetIntent>.toPartialStateChangeFlow(): Flow<BottomSheetStateChange> {
        return merge(
            filterIsInstance<BottomSheetIntent.GetCategories>()
                .flatMapLatest {
                    combine(
                        categoryRepository.getCategoryList(),
                        iconRepository.list(),
                    ) { categories, icons ->
                        this@BottomSheetViewModel.categories = categories
                        this@BottomSheetViewModel.iconMap = icons.associate { it.id to it.resId }
                        BottomSheetStateChange.SheetData.Init(
                            selectedCategoryId = categories[0].id,
                            iconItems = categories.map { Triple(it.name, it.id, iconMap[it.iconId] ?: IconBean.DEFAULT_ICON) },
                            1
                        )
                    }
                }.startWith(BottomSheetStateChange.SheetData.Loading),


            filterNot { it is BottomSheetIntent.GetCategories }
                .flatMapLatest { intent ->
                    when (intent) {
                        is BottomSheetIntent.BackCategory -> {
                            flow<BottomSheetStateChange> {
                                BottomSheetStateChange.SheetData.BackCategory
                            }
                        }
                        is BottomSheetIntent.CategoryPage -> {
                            flow<BottomSheetStateChange> {
                                BottomSheetStateChange.SheetData.CategoryPage(intent.page)
                            }
                        }
                        is BottomSheetIntent.TypePage -> {
                            flow<BottomSheetStateChange> {
                                BottomSheetStateChange.SheetData.TypePage(intent.page)
                            }
                        }
                        is BottomSheetIntent.GetTypes -> {
                            typeRepository.getTypeList(intent.categoryId).transform { types ->
                                emit(
                                    BottomSheetStateChange.SheetData.GetType(
                                        types.map { Triple(it.name, it.id, intent.icon) },
                                        intent.categoryId
                                    )
                                )
                            }
                        }
                        is BottomSheetIntent.Save -> {
                            viewModelScope.launch(Dispatchers.IO) {
                                billRepository.insert(intent.bill)
                                sendEvent(BottomSheetEvent.SaveSuccess)
                            }
                            emptyFlow<BottomSheetStateChange>()
                        }
                        is BottomSheetIntent.SelectType -> {
                            flow {
                                emit(
                                    BottomSheetStateChange.SheetData.SelectType(intent.typeId)
                                )
                            }
                        }
                        else -> {
                            emptyFlow<BottomSheetStateChange>()
                        }
                    }
                }.startWith(BottomSheetStateChange.SheetData.Loading)

        )
    }
}