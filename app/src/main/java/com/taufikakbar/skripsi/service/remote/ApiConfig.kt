package com.taufikakbar.skripsi.service.remote

import com.taufikakbar.skripsi.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class ApiConfig {
    companion object {
        fun getApiService(url: String): ApiService {
            val loggingInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor).build()
            val retrofit = Retrofit.Builder().baseUrl(url)
                .client(client).build()

            return retrofit.create(ApiService::class.java)
        }
    }
}