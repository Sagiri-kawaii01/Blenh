package com.github.sagiri_kawaii01.blenh.model.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.sagiri_kawaii01.blenh.model.bean.CATEGORY_TABLE_NAME
import com.github.sagiri_kawaii01.blenh.model.bean.CategoryBean
import com.github.sagiri_kawaii01.blenh.model.bean.TYPE_TABLE_NAME
import com.github.sagiri_kawaii01.blenh.model.bean.TypeBean

object Migration_2_3: Migration(2, 3) {

    private val newCategories = listOf(
        "娱乐" to 1,
        "教育" to 1,
        "金融" to 1,
        "其他" to 1
    )

    private val newTypes = listOf(
        1 to "零食",
        1 to "饮料",
        1 to "奶茶",
        1 to "外卖",
        1 to "其他",
        3 to "打车",
        3 to "共享单车",
        3 to "小溜",
        3 to "火车",
        3 to "飞机",
        3 to "汽油",
        3 to "停车费",
        3 to "其他",
        2 to "鞋子",
        2 to "电子产品",
        2 to "日用品",
        2 to "电器",
        2 to "家具",
        2 to "化妆品",
        2 to "图书",
        2 to "其他",
        6 to "药品",
        6 to "门诊",
        6 to "住院",
        6 to "保健品",
        6 to "体检",
        6 to "疫苗",
        6 to "其他",
        5 to "房租",
        5 to "物业费",
        5 to "水费",
        5 to "电费",
        5 to "燃气费",
        5 to "宽带",
        5 to "电话费",
        5 to "其他",
        9 to "电影",
        9 to "音乐",
        9 to "氪金",
        9 to "游戏",
        9 to "旅游",
        9 to "宠物",
        9 to "运动",
        9 to "开房",
        9 to "其他",
        10 to "学费",
        10 to "书籍",
        10 to "文具",
        10 to "培训",
        10 to "学杂费",
        10 to "在线课程",
        10 to "其他",
        11 to "贷款",
        11 to "利息",
        11 to "保险",
        11 to "投资",
        11 to "手续费",
        11 to "税款",
        11 to "其他",
        12 to "其他"
    )

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE $CATEGORY_TABLE_NAME ADD COLUMN ${CategoryBean.SORT_COLUMN} INTEGER DEFAULT 0")
        db.execSQL("ALTER TABLE $TYPE_TABLE_NAME ADD COLUMN ${TypeBean.SORT_COLUMN} INTEGER DEFAULT 0")
        db.execSQL("DELETE FROM $CATEGORY_TABLE_NAME WHERE id = 7")
        db.execSQL("DELETE FROM $CATEGORY_TABLE_NAME WHERE id = 8")
        db.execSQL("UPDATE $TYPE_TABLE_NAME SET ${TypeBean.NAME_COLUMN} = '早餐' WHERE id = 1")
        db.execSQL("UPDATE $TYPE_TABLE_NAME SET ${TypeBean.NAME_COLUMN} = '午餐' WHERE id = 2")
        db.execSQL("UPDATE $TYPE_TABLE_NAME SET ${TypeBean.NAME_COLUMN} = '晚餐' WHERE id = 3")
        db.execSQL("UPDATE $TYPE_TABLE_NAME SET ${TypeBean.NAME_COLUMN} = '服装' WHERE id = 4")


        newCategories.forEach {
            db.execSQL("INSERT INTO $CATEGORY_TABLE_NAME (${CategoryBean.NAME_COLUMN}, ${CategoryBean.ICON_ID_COLUMN}) VALUES ('${it.first}', ${it.second})")
        }

        newTypes.forEach {
            db.execSQL("INSERT INTO $TYPE_TABLE_NAME (${TypeBean.CATEGORY_ID_COLUMN}, ${TypeBean.NAME_COLUMN}) VALUES (${it.first}, '${it.second}')")
        }

        db.execSQL("UPDATE $TYPE_TABLE_NAME SET ${TypeBean.SORT_COLUMN} = ${TypeBean.ID_COLUMN}")
        db.execSQL("UPDATE $CATEGORY_TABLE_NAME SET ${CategoryBean.SORT_COLUMN} = ${CategoryBean.ID_COLUMN}")
        db.execSQL("UPDATE $TYPE_TABLE_NAME SET ${TypeBean.SORT_COLUMN} = 99 WHERE ${TypeBean.NAME_COLUMN} = '其他'")
        db.execSQL("UPDATE $CATEGORY_TABLE_NAME SET ${CategoryBean.SORT_COLUMN} = 99 WHERE ${CategoryBean.NAME_COLUMN} = '其他'")
    }
}