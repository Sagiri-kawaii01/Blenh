package com.github.sagiri_kawaii01.blenh.di

import android.content.Context
import com.github.sagiri_kawaii01.blenh.model.db.AppDatabase
import com.github.sagiri_kawaii01.blenh.model.db.dao.BillDao
import com.github.sagiri_kawaii01.blenh.model.db.dao.CategoryDao
import com.github.sagiri_kawaii01.blenh.model.db.dao.IconDao
import com.github.sagiri_kawaii01.blenh.model.db.dao.TypeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.annotation.Signed
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        return AppDatabase.getInstance(context, scope)
    }

    @Provides
    @Singleton
    fun provideIconDao(database: AppDatabase): IconDao = database.iconDao()

    @Provides
    @Singleton
    fun provideBillDao(database: AppDatabase): BillDao = database.billDao()

    @Provides
    @Singleton
    fun provideTypeDao(database: AppDatabase): TypeDao = database.typeDao()

    @Provides
    @Singleton
    fun provideCategoryDao(database: AppDatabase): CategoryDao = database.categoryDao()
}