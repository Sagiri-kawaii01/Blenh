package com.github.sagiri_kawaii01.blenh.model.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.github.sagiri_kawaii01.blenh.base.BaseBean
import kotlinx.serialization.Serializable


const val CATEGORY_TABLE_NAME = "Category"

@Serializable
@Entity(
    tableName = CATEGORY_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = IconBean::class,
            parentColumns = [IconBean.ID_COLUMN],
            childColumns = [CategoryBean.ICON_ID_COLUMN],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = [CategoryBean.ICON_ID_COLUMN], unique = false)
    ]
)
data class CategoryBean(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID_COLUMN)
    val id: Int = 0,
    @ColumnInfo(name = NAME_COLUMN)
    val name: String,
    @ColumnInfo(name = ICON_ID_COLUMN)
    val iconId: Int,
    @ColumnInfo(name = SORT_COLUMN, defaultValue = "0")
    val sort: Int
): BaseBean {
    companion object {
        const val ID_COLUMN = "id"
        const val NAME_COLUMN = "name"
        const val ICON_ID_COLUMN = "iconId"
        const val SORT_COLUMN = "sort"

        val CategoryList = listOf(
            CategoryBean(name = "饮食", iconId = 1, sort = 0),
            CategoryBean(name = "购物", iconId = 2, sort = 1),
            CategoryBean(name = "交通", iconId = 3, sort = 2),
            CategoryBean(name = "娱乐", iconId = 4, sort = 3),
            CategoryBean(name = "宠物", iconId = 5, sort = 4),
            CategoryBean(name = "生活缴费", iconId = 6, sort = 5),
            CategoryBean(name = "医疗", iconId = 7, sort = 6),
            CategoryBean(name = "教育", iconId = 8, sort = 7),
            CategoryBean(name = "其他", iconId = 9, sort = 99),
            CategoryBean(name = "金融", iconId = 10, sort = 8),
        )
    }
}
