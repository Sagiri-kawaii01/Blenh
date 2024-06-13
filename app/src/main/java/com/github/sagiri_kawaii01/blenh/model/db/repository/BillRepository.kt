package com.github.sagiri_kawaii01.blenh.model.db.repository

import com.github.sagiri_kawaii01.blenh.model.TimePeriodType
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.model.db.dao.BillDao
import com.github.sagiri_kawaii01.blenh.model.vo.BillChart
import com.github.sagiri_kawaii01.blenh.util.flowOnIo
import com.github.sagiri_kawaii01.blenh.util.today
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class BillRepository @Inject constructor(
    private val billDao: BillDao
) {

    fun insert(billBean: BillBean) = billDao.insert(billBean)

    fun list(type: TimePeriodType): Flow<List<BillBean>> {
        val today = today()
        return billDao.listWeekEndWith(today.year, today.monthValue, today.dayOfMonth).flowOnIo()
    }

    fun expend(type: TimePeriodType): Flow<Pair<Double, Double>> {
        var end = today()
        var start = end.getStart(type)

        val current = billDao.totalBetween(
            start.year,
            start.monthValue,
            start.dayOfMonth,
            end.year,
            end.monthValue,
            end.dayOfMonth,
        ).flowOnIo()

        end = start.minusDays(1)
        start = end.getStart(type)

        val last = billDao.totalBetween(
            start.year,
            start.monthValue,
            start.dayOfMonth,
            end.year,
            end.monthValue,
            end.dayOfMonth,
        ).flowOnIo()

        return combine(current, last) { c, l ->
            Pair(c, l)
        }
    }

    fun charts(type: TimePeriodType): Flow<List<BillChart>> {
        return when (type) {
            TimePeriodType.Week -> {
                billDao.chartsWeek().flowOnIo()
            }
            TimePeriodType.Month -> {
                billDao.chartsMonth().flowOnIo()
            }
            TimePeriodType.Year -> {
                billDao.chartsYear().flowOnIo()
            }
            else -> emptyFlow()
        }
    }

}

private fun LocalDate.getStart(type: TimePeriodType): LocalDate {
    return when (type) {
        TimePeriodType.Week -> this.minusDays(this.dayOfWeek.value - 1L)
        TimePeriodType.Month -> this.minusDays(this.dayOfMonth - 1L)
        TimePeriodType.Year -> this.minusDays(this.dayOfYear - 1L)
        else -> this
    }
}