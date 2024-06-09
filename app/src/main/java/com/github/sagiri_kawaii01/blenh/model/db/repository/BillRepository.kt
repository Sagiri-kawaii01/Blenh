package com.github.sagiri_kawaii01.blenh.model.db.repository

import com.github.sagiri_kawaii01.blenh.model.TimePeriodType
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.model.db.dao.BillDao
import com.github.sagiri_kawaii01.blenh.util.today
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

class BillRepository @Inject constructor(
    private val billDao: BillDao
) {
    fun list(type: TimePeriodType): Flow<List<BillBean>> {
        val today = today()
        return when (type) {
            TimePeriodType.Week -> {
                billDao.listWeekEndWith(today.year, today.monthValue, today.dayOfMonth).flowOn(Dispatchers.IO)
            }

            else -> emptyFlow()
        }
    }
}