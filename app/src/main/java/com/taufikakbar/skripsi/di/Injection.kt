package com.taufikakbar.skripsi.di

import com.taufikakbar.skripsi.data.EspRepository
import com.taufikakbar.skripsi.service.remote.ApiConfig

object Injection {
    fun provideRepository(url: String): EspRepository {
        val apiService = ApiConfig.getApiService(url)
        return EspRepository(apiService)
    }
}