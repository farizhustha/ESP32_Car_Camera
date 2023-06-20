package com.taufikakbar.skripsi.ui.config

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.taufikakbar.skripsi.databinding.ActivityConfigBinding
import com.taufikakbar.skripsi.ui.home.HomeActivity
import com.taufikakbar.skripsi.data.Result

class ConfigActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfigBinding
    private val viewModel: ConfigViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView() {
        binding.edtConfigIp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setIpAddress(char.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun setupAction() {
        viewModel.ipAddress.observe(this) { text ->
            val configText = "IP Address: $text"
            binding.apply {
                tvConfigIp.text = configText
                btnConfigStart.setOnClickListener {
                    viewModel.checkServer(text).observe(this@ConfigActivity) { result ->
                        when (result) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                val intent = Intent(this@ConfigActivity, HomeActivity::class.java)
                                intent.putExtra(HomeActivity.URL_KEY, viewModel.ipAddress.value)
                                startActivity(intent)
                            }
                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    this@ConfigActivity,
                                    "Gagal connect ke ESP32: ${result.error}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }
}