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
    val name: String,
    @ColumnInfo(name = SORT_COLUMN, defaultValue = "0")
    val sort: Int
): BaseBean {
    companion object {
        const val ID_COLUMN = "id"
        const val CATEGORY_ID_COLUMN = "categoryId"
        const val NAME_COLUMN = "name"
        const val SORT_COLUMN = "sort"

        val TypeList = listOf(
            TypeBean(categoryId = 1, name = "早餐", sort = 0),
            TypeBean(categoryId = 1, name = "午餐", sort = 1),
            TypeBean(categoryId = 1, name = "晚餐", sort = 2),
            TypeBean(categoryId = 1, name = "零食", sort = 3),
            TypeBean(categoryId = 1, name = "饮料", sort = 4),
            TypeBean(categoryId = 1, name = "奶茶", sort = 5),
            TypeBean(categoryId = 1, name = "外卖", sort = 6),
            TypeBean(categoryId = 1, name = "其他", sort = 99),
            TypeBean(categoryId = 2, name = "服装", sort = 0),
            TypeBean(categoryId = 2, name = "电子产品", sort = 1),
            TypeBean(categoryId = 2, name = "日用品", sort = 2),
            TypeBean(categoryId = 2, name = "电器", sort = 3),
            TypeBean(categoryId = 2, name = "家具", sort = 4),
            TypeBean(categoryId = 2, name = "化妆品", sort = 5),
            TypeBean(categoryId = 2, name = "其他", sort = 99),
            TypeBean(categoryId = 3, name = "地铁", sort = 0),
            TypeBean(categoryId = 3, name = "公交", sort = 1),
            TypeBean(categoryId = 3, name = "打车", sort = 2),
            TypeBean(categoryId = 3, name = "共享单车", sort = 3),
            TypeBean(categoryId = 3, name = "小溜", sort = 4),
            TypeBean(categoryId = 3, name = "火车", sort = 5),
            TypeBean(categoryId = 3, name = "飞机", sort = 6),
            TypeBean(categoryId = 3, name = "汽油", sort = 7),
            TypeBean(categoryId = 3, name = "停车费", sort = 8),
            TypeBean(categoryId = 3, name = "其他", sort = 99),
            TypeBean(categoryId = 4, name = "电影", sort = 0),
            TypeBean(categoryId = 4, name = "音乐", sort = 1),
            TypeBean(categoryId = 4, name = "氪金", sort = 2),
            TypeBean(categoryId = 4, name = "游戏", sort = 3),
            TypeBean(categoryId = 4, name = "旅游", sort = 4),
            TypeBean(categoryId = 4, name = "运动", sort = 5),
            TypeBean(categoryId = 4, name = "开房", sort = 6),
            TypeBean(categoryId = 4, name = "其他", sort = 99),
            TypeBean(categoryId = 5, name = "宠物食品", sort = 0),
            TypeBean(categoryId = 5, name = "宠物用品", sort = 1),
            TypeBean(categoryId = 5, name = "宠物服装", sort = 2),
            TypeBean(categoryId = 5, name = "宠物医疗", sort = 3),
            TypeBean(categoryId = 5, name = "宠物领养", sort = 4),
            TypeBean(categoryId = 5, name = "宠物玩具", sort = 5),
            TypeBean(categoryId = 5, name = "其他", sort = 99),
            TypeBean(categoryId = 7, name = "药品", sort = 0),
            TypeBean(categoryId = 7, name = "门诊", sort = 1),
            TypeBean(categoryId = 7, name = "住院", sort = 2),
            TypeBean(categoryId = 7, name = "保健品", sort = 3),
            TypeBean(categoryId = 7, name = "体检", sort = 4),
            TypeBean(categoryId = 7, name = "疫苗", sort = 5),
            TypeBean(categoryId = 7, name = "其他", sort = 99),
            TypeBean(categoryId = 6, name = "房租", sort = 0),
            TypeBean(categoryId = 6, name = "水费", sort = 1),
            TypeBean(categoryId = 6, name = "电费", sort = 2),
            TypeBean(categoryId = 6, name = "物业费", sort = 3),
            TypeBean(categoryId = 6, name = "燃气费", sort = 4),
            TypeBean(categoryId = 6, name = "宽带", sort = 5),
            TypeBean(categoryId = 6, name = "电话费", sort = 6),
            TypeBean(categoryId = 6, name = "其他", sort = 99),
            TypeBean(categoryId = 8, name = "学费", sort = 0),
            TypeBean(categoryId = 8, name = "书籍", sort = 1),
            TypeBean(categoryId = 8, name = "文具", sort = 2),
            TypeBean(categoryId = 8, name = "培训", sort = 3),
            TypeBean(categoryId = 8, name = "学杂费", sort = 4),
            TypeBean(categoryId = 8, name = "在线课程", sort = 5),
            TypeBean(categoryId = 8, name = "其他", sort = 99),
            TypeBean(categoryId = 9, name = "其他", sort = 99),
            TypeBean(categoryId = 10, name = "贷款", sort = 0),
            TypeBean(categoryId = 10, name = "利息", sort = 1),
            TypeBean(categoryId = 10, name = "保险", sort = 2),
            TypeBean(categoryId = 10, name = "投资", sort = 3),
            TypeBean(categoryId = 10, name = "手续费", sort = 4),
            TypeBean(categoryId = 10, name = "税款", sort = 5),
            TypeBean(categoryId = 10, name = "其他", sort = 99),
        )
    }
}