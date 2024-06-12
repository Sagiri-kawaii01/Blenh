package com.github.sagiri_kawaii01.blenh.model.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.sagiri_kawaii01.blenh.model.bean.BILL_TABLE_NAME
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean

object Migration_1_2: Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE $BILL_TABLE_NAME ADD COLUMN ${BillBean.PAY_METHOD_COLUMN} TEXT")
        database.execSQL("ALTER TABLE $BILL_TABLE_NAME ADD COLUMN ${BillBean.TARGET_COLUMN} TEXT")
    }
}