package com.github.sagiri_kawaii01.blenh.model.db.repository

import com.github.sagiri_kawaii01.blenh.model.db.dao.CategoryDao
import com.github.sagiri_kawaii01.blenh.util.flowOnIo
import javax.inject.Inject
import javax.inject.Singleton


class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {
    fun getCategoryList() = categoryDao.getCategoryList().flowOnIo()
    fun categoryList() = categoryDao.categoryList()
    fun getById(id: Int) = categoryDao.getById(id)
}