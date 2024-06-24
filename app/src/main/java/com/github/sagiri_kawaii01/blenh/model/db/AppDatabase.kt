package com.github.sagiri_kawaii01.blenh.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.sagiri_kawaii01.blenh.BuildConfig
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.model.bean.CategoryBean
import com.github.sagiri_kawaii01.blenh.model.bean.IconBean
import com.github.sagiri_kawaii01.blenh.model.bean.TypeBean
import com.github.sagiri_kawaii01.blenh.model.db.dao.BillDao
import com.github.sagiri_kawaii01.blenh.model.db.dao.CategoryDao
import com.github.sagiri_kawaii01.blenh.model.db.dao.IconDao
import com.github.sagiri_kawaii01.blenh.model.db.dao.TypeDao
import com.github.sagiri_kawaii01.blenh.model.db.migration.Migration_1_2
import com.github.sagiri_kawaii01.blenh.model.db.migration.Migration_2_3
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val APP_DATA_BASE_FILE_NAME = "app.db"

@Database(
    entities = [
        IconBean::class,
        CategoryBean::class,
        TypeBean::class,
        BillBean::class
    ],
    version = 3
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun iconDao(): IconDao
    abstract fun categoryDao(): CategoryDao
    abstract fun typeDao(): TypeDao
    abstract fun billDao(): BillDao

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ): Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            instance?.let { database ->
                scope.launch {
                    populateDatabase(
                        database.iconDao(),
                        database.categoryDao(),
                        database.typeDao(),
                        database.billDao()
                    )
                }
            }
        }

        suspend fun populateDatabase(iconDao: IconDao,
                                     categoryDao: CategoryDao,
                                     typeDao: TypeDao,
                                     billDao: BillDao) {
            for (index in IconBean.IconList.indices) {
                iconDao.insert(IconBean(resId = index))
            }
            CategoryBean.CategoryList.forEach {
                categoryDao.insert(it)
            }

            TypeBean.TypeList.forEach {
                typeDao.insert(it)
            }

            if (BuildConfig.DEBUG) {
                BillBean.TEST_DATA.forEach {
                    billDao.insert(it)
                }
            }
        }
    }

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        private val migrations = arrayOf<Migration>(
            Migration_1_2,
            Migration_2_3
        )

        fun getInstance(context: Context, scope: CoroutineScope): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    APP_DATA_BASE_FILE_NAME
                )
                    .addMigrations(*migrations)
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                    .apply { instance = this }
            }
        }
    }
}