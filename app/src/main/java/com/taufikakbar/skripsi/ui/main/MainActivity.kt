package com.taufikakbar.skripsi.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.taufikakbar.skripsi.R
import com.taufikakbar.skripsi.ui.config.ConfigActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, ConfigActivity::class.java)
        startActivity(intent)
        finish()
    }
}