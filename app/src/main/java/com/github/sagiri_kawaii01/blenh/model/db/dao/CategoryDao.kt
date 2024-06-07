package com.github.sagiri_kawaii01.blenh.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.github.sagiri_kawaii01.blenh.model.bean.CategoryBean

@Dao
interface CategoryDao {
    @Insert
    suspend fun insert(category: CategoryBean)
}