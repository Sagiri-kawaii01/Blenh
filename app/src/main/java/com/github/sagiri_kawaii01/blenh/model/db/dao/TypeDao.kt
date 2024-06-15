package com.github.sagiri_kawaii01.blenh.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.sagiri_kawaii01.blenh.model.bean.TYPE_TABLE_NAME
import com.github.sagiri_kawaii01.blenh.model.bean.TypeBean
import kotlinx.coroutines.flow.Flow

@Dao
interface TypeDao {
    @Insert
    fun insert(type: TypeBean)

    @Query("SELECT * FROM $TYPE_TABLE_NAME")
    fun getTypeList(): List<TypeBean>

    @Query("SELECT * FROM $TYPE_TABLE_NAME WHERE ${TypeBean.CATEGORY_ID_COLUMN} = :cateGoryId")
    fun getTypeList(cateGoryId: Int): Flow<List<TypeBean>>


}