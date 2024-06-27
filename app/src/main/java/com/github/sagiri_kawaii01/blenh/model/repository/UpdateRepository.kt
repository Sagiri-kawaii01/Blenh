package com.github.sagiri_kawaii01.blenh.model.repository

import UpdateBean
import com.github.sagiri_kawaii01.blenh.base.BaseRepository
import com.github.sagiri_kawaii01.blenh.model.service.UpdateService
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import javax.inject.Inject


class UpdateRepository @Inject constructor(private val retrofit: Retrofit) : BaseRepository() {
    suspend fun checkUpdate(): Flow<UpdateBean> {
        return flowOnIo {
            emit(retrofit.create(UpdateService::class.java).checkUpdate())
        }
    }
}