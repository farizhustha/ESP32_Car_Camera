package com.taufikakbar.skripsi.service.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/action")
    suspend fun setAction(@Query("go") go: String): ResponseBody

    @GET("/led")
    suspend fun setFlash(@Query("flash") flash: String): ResponseBody

}