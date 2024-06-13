package com.github.sagiri_kawaii01.blenh.model.db.repository

import com.github.sagiri_kawaii01.blenh.model.db.dao.IconDao
import com.github.sagiri_kawaii01.blenh.util.flowOnIo
import javax.inject.Inject
import javax.inject.Singleton


class IconRepository @Inject constructor(
    private val iconDao: IconDao
) {
    fun list() = iconDao.list().flowOnIo()
}