package com.taufikakbar.skripsi.ui.config

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.taufikakbar.skripsi.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class ConfigViewModel : ViewModel() {
    private val _ipAddress = MutableLiveData<String>()
    val ipAddress: LiveData<String> = _ipAddress

    private val client = OkHttpClient()

    fun setIpAddress(char: String) {
        _ipAddress.value = char
    }

    fun checkServer(ipAddress: String): LiveData<Result<Boolean>> = liveData {
        emit(Result.Loading)
        val url = "http://$ipAddress/"
        try {
            val request = Request.Builder()
                .url(url).build()
            withContext(Dispatchers.IO) {
                val response = client.newCall(request).execute()
                emit(Result.Success(response.isSuccessful))
            }

        } catch (e: IOException) {
            emit(Result.Error(e.message.toString()))
        }
    }
}