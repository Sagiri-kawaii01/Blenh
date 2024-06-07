package com.github.sagiri_kawaii01.blenh.model.db.repository

import com.github.sagiri_kawaii01.blenh.model.db.dao.TypeDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TypeRepository @Inject constructor(
    private val typeDao: TypeDao
) {
}