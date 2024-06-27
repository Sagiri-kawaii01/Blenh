package com.github.sagiri_kawaii01.blenh.model.service

import UpdateBean
import retrofit2.http.GET

interface UpdateService {
    @GET("https://api.github.com/repos/Sagiri-kawaii01/blenh/releases/latest")
    suspend fun checkUpdate(): UpdateBean
}