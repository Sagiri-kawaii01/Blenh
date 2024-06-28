package com.github.sagiri_kawaii01.blenh.model.service

import UpdateBean
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface UpdateService {
    @GET("https://api.github.com/repos/Sagiri-kawaii01/blenh/releases/latest")
    suspend fun checkUpdate(): UpdateBean

    @GET
    @Streaming
    fun downloadFile(@Url fileUrl: String): Call<ResponseBody>
}