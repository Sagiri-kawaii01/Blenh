package com.github.sagiri_kawaii01.blenh.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.sagiri_kawaii01.blenh.model.bean.CATEGORY_TABLE_NAME
import com.github.sagiri_kawaii01.blenh.model.bean.CategoryBean
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert
    suspend fun insert(category: CategoryBean)

    @Query("SELECT * FROM $CATEGORY_TABLE_NAME ORDER BY ${CategoryBean.SORT_COLUMN}")
    fun getCategoryList(): Flow<List<CategoryBean>>

    @Query("SELECT * FROM $CATEGORY_TABLE_NAME ORDER BY ${CategoryBean.SORT_COLUMN}")
    fun categoryList(): List<CategoryBean>

    @Query("SELECT * FROM $CATEGORY_TABLE_NAME WHERE ${CategoryBean.ID_COLUMN} = :id")
    fun getById(id: Int): CategoryBean
}