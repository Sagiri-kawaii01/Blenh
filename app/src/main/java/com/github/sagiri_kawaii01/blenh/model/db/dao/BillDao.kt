package com.github.sagiri_kawaii01.blenh.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.sagiri_kawaii01.blenh.model.bean.BILL_TABLE_NAME
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.model.vo.BillChart
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {
    @Insert
    fun insert(bill: BillBean)

    @Query("""
        SELECT * FROM $BILL_TABLE_NAME
        WHERE DATE(year || '-' || printf('%02d', month) || '-' || printf('%02d', day)) BETWEEN 
              DATE(printf('%04d-%02d-%02d', :year, :month, :day), '-6 days')
              AND DATE(printf('%04d-%02d-%02d', :year, :month, :day))
        ORDER BY datetime(year || '-' || printf('%02d', month) || '-' || printf('%02d', day) || ' ' || time) DESC
    """)
    fun listWeekEndWith(year: Int, month: Int, day: Int): Flow<List<BillBean>>

    @Query("""
        SELECT SUM(money) FROM $BILL_TABLE_NAME
        WHERE year || '-' || printf('%02d', month) || '-' || printf('%02d', day) BETWEEN 
              printf('%04d-%02d-%02d', :sYear, :sMonth, :sDay)
              AND printf('%04d-%02d-%02d', :eYear, :eMonth, :eDay)
    """)
    fun totalBetween(sYear: Int, sMonth: Int, sDay: Int, eYear: Int, eMonth: Int, eDay: Int): Flow<Double>

    @Query("""
        SELECT year, month, day, SUM(money) AS money
        FROM $BILL_TABLE_NAME
        WHERE
            (year || '-' || printf('%02d', month) || '-' || printf('%02d', day)) BETWEEN
            date('now', '-7 days') AND date('now')
        GROUP BY
            year,
            month,
            day
        ORDER BY
            year ASC,
            month ASC,
            day ASC;
    """)
    fun chartsWeek(): Flow<List<BillChart>>


    @Query("""
        SELECT year, month, day, SUM(money) AS money
        FROM $BILL_TABLE_NAME
        WHERE
            year = strftime('%Y', 'now') AND
            month = strftime('%m', 'now')
        GROUP BY
            year,
            month,
            day
        ORDER BY
            year ASC,
            month ASC,
            day ASC;
    """)
    fun chartsMonth(): Flow<List<BillChart>>

    @Query("""
        SELECT year, month, SUM(money) AS money
        FROM $BILL_TABLE_NAME
        WHERE
            year = strftime('%Y', 'now')
        GROUP BY
            year,
            month
        ORDER BY
            year ASC,
            month ASC;
    """)
    fun chartsYear(): Flow<List<BillChart>>

}