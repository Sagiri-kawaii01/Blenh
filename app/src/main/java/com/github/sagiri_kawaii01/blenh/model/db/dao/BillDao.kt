package com.github.sagiri_kawaii01.blenh.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.sagiri_kawaii01.blenh.model.bean.BILL_TABLE_NAME
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
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
}