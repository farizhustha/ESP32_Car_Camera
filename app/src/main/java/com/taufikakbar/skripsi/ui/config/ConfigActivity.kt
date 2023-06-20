package com.taufikakbar.skripsi.ui.config

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.taufikakbar.skripsi.databinding.ActivityConfigBinding
import com.taufikakbar.skripsi.ui.home.HomeActivity

class ConfigActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfigBinding
    private val viewModel: ConfigViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.ipAddress.observe(this) { text ->
            val configText = "IP Address: $text"
            binding.apply {
                tvConfigIp.text = configText
                btnConfigStart.setOnClickListener {
                    viewModel.checkServer(text)
                }
            }
        }

        viewModel.statusServer.observe(this) { state ->
            if (state) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra(HomeActivity.URL_KEY, viewModel.ipAddress.value)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Gagal connect ke ESP32", Toast.LENGTH_SHORT).show()
            }
        }

        binding.apply {
            edtConfigIp.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel.setIpAddress(char.toString())
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })
        }
    }
}