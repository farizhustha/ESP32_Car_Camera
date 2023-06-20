package com.taufikakbar.skripsi.ui.config

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.net.MalformedURLException

class ConfigViewModel : ViewModel() {
    private val _ipAddress = MutableLiveData<String>()
    val ipAddress: LiveData<String> = _ipAddress

    private val _statusServer = MutableLiveData<Boolean>()
    val statusServer: LiveData<Boolean> = _statusServer

    private val client = OkHttpClient()

    fun setIpAddress(char: String) {
        _ipAddress.value = char
    }

    fun checkServer(ipAddress: String) {
        val url = "http://$ipAddress/"
        try {
            val request = Request.Builder()
                .url(url).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    viewModelScope.launch {
                        _statusServer.value = response.isSuccessful
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    viewModelScope.launch {
                        _statusServer.value = false
                    }
                }
            })
        } catch (_: MalformedURLException) {
            viewModelScope.launch {
                _statusServer.value = false
            }

        }
    }
}