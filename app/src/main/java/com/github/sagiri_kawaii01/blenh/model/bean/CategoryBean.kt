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
    val iconId: Int
): BaseBean {
    companion object {
        const val ID_COLUMN = "id"
        const val NAME_COLUMN = "name"
        const val ICON_ID_COLUMN = "iconId"

        val CategoryList = listOf(
            "饮食",
            "购物",
            "交通",
            "宠物",
            "生活缴费",
            "医疗",
            "赡养",
            "运动",
        )
    }
}
