package com.taufikakbar.skripsi.data

import com.taufikakbar.skripsi.service.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EspRepository(private val apiService: ApiService) {
    suspend fun setAction(direction: String) {
        withContext(Dispatchers.IO) {
            apiService.setAction(direction)
        }
    }

    suspend fun setFlash(state: String) {
        withContext(Dispatchers.IO) {
            apiService.setFlash(state)
        }
    }
}