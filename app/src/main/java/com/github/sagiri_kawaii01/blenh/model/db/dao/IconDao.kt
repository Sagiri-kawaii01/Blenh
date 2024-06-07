package com.github.sagiri_kawaii01.blenh.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.sagiri_kawaii01.blenh.model.bean.ICON_TABLE_NAME
import com.github.sagiri_kawaii01.blenh.model.bean.IconBean
import kotlinx.coroutines.flow.Flow

@Dao
interface IconDao {
    @Insert
    fun insert(icon: IconBean)

    @Query("SELECT * FROM $ICON_TABLE_NAME")
    fun list(): List<IconBean>
}