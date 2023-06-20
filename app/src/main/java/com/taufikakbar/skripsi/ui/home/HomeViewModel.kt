package com.taufikakbar.skripsi.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taufikakbar.skripsi.data.EspRepository
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.*

class HomeViewModel(private val espRepository: EspRepository) : ViewModel() {
    private val _imageData = MutableLiveData<Bitmap>()
    val imageData: LiveData<Bitmap> = _imageData

    private val _isFLash = MutableLiveData(false)
    val isFlash: LiveData<Boolean> = _isFLash

    fun setAction(direction: String) {
        viewModelScope.launch {
            espRepository.setAction(direction)
        }
    }

    fun setFlashState(state: Boolean) {
        _isFLash.value = state
    }

    fun setFlash(state: String) {
        viewModelScope.launch {
            espRepository.setFlash(state)
        }
    }

    fun startStreaming(ipAddress: String) {
        val streamUrl = "http://$ipAddress:81/stream"
        var bufferInputStream: BufferedInputStream? = null
        val fileOutputStream: FileOutputStream? = null

        try {
            val request = Request.Builder()
                .url(streamUrl).build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseBody = response.body
                        responseBody?.let { body ->
                            val inputStream = body.byteStream()
                            val inputStreamReader = InputStreamReader(inputStream)
                            val bufferedReader = BufferedReader(inputStreamReader)
                            var data: String
                            var len: Int
                            var buffer: ByteArray
                            while (bufferedReader.readLine().also { data = it } != null) {
                                if (data.contains("Content-Type:")) {
                                    data = bufferedReader.readLine()
                                    len = data.split(":".toRegex()).dropLastWhile {
                                        it.isEmpty()
                                    }.toTypedArray()[1].trim { it <= ' ' }.toInt()
                                    bufferInputStream = BufferedInputStream(inputStream)
                                    buffer = ByteArray(len)
                                    var t = 0
                                    while (t < len) {
                                        bufferInputStream?.let {
                                            t += it.read(buffer, t, len - t)
                                        }
                                    }
                                    val bitmap =
                                        BitmapFactory.decodeByteArray(buffer, 0, buffer.size)

                                    viewModelScope.launch {
                                        _imageData.value = bitmap
                                    }
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                }
            })
        } catch (_: IllegalStateException) {

        } finally {
            try {
                bufferInputStream?.close()
                fileOutputStream?.close()
            } catch (_: IOException) {

            }
        }
    }
}