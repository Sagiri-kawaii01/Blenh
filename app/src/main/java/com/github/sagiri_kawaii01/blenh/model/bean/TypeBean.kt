package com.github.sagiri_kawaii01.blenh.model.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.github.sagiri_kawaii01.blenh.base.BaseBean
import kotlinx.serialization.Serializable


const val TYPE_TABLE_NAME = "Type"

@Serializable
@Entity(
    tableName = TYPE_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = CategoryBean::class,
            parentColumns = [CategoryBean.ID_COLUMN],
            childColumns = [TypeBean.CATEGORY_ID_COLUMN],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = [TypeBean.CATEGORY_ID_COLUMN], unique = false)
    ]
)
data class TypeBean(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID_COLUMN)
    val id: Int = 0,
    @ColumnInfo(name = CATEGORY_ID_COLUMN)
    val categoryId: Int,
    @ColumnInfo(name = NAME_COLUMN)
    val name: String
): BaseBean {
    companion object {
        const val ID_COLUMN = "id"
        const val CATEGORY_ID_COLUMN = "categoryId"
        const val NAME_COLUMN = "name"

        val TEST_DATA = listOf(
            TypeBean(categoryId = 1, name = "早饭"),
            TypeBean(categoryId = 1, name = "午饭"),
            TypeBean(categoryId = 1, name = "晚饭"),
            TypeBean(categoryId = 2, name = "购物"),
            TypeBean(categoryId = 3, name = "地铁"),
            TypeBean(categoryId = 3, name = "公交"),
            TypeBean(categoryId = 5, name = "水费"),
            TypeBean(categoryId = 5, name = "电费"),
        )
    }
}