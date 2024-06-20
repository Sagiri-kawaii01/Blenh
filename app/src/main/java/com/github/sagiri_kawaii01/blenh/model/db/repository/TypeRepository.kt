package com.github.sagiri_kawaii01.blenh.model.db.repository

import com.github.sagiri_kawaii01.blenh.model.db.dao.TypeDao
import com.github.sagiri_kawaii01.blenh.util.flowOnIo
import javax.inject.Inject
import javax.inject.Singleton

class TypeRepository @Inject constructor(
    private val typeDao: TypeDao
) {
    fun getTypeList() = typeDao.getTypeList()
    fun getTypeFlow() = typeDao.getTypeFlow()
    fun getTypeList(categoryId: Int) = typeDao.getTypeList(categoryId).flowOnIo()
}