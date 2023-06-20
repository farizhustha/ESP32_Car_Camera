package com.taufikakbar.skripsi.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.taufikakbar.skripsi.R
import com.taufikakbar.skripsi.databinding.ActivityHomeBinding
import com.taufikakbar.skripsi.ui.ViewModelFactory


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupObserver()
        setupAction()
    }

    private fun setupViewModel() {
        val ipAddress = intent.getStringExtra(URL_KEY) ?: ""
        val factory = ViewModelFactory("http://$ipAddress")

        viewModel = ViewModelProvider(this, factory)[
                HomeViewModel::class.java
        ]

        viewModel.startStreaming(ipAddress)
    }

    private fun setupObserver() {
        viewModel.imageData.observe(this) { image ->
            binding.imgHomeStream.setImageBitmap(image)
        }

        viewModel.isFlash.observe(this) { state ->
            if (state) {
                viewModel.setFlash("on")
                binding.btnHomeFlash.setImageResource(R.drawable.ic_flashlight_on_24)
            } else {
                viewModel.setFlash("off")
                binding.btnHomeFlash.setImageResource(R.drawable.ic_flashlight_off_24)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupAction() {
        binding.apply {
            btnHomeForward.setOnTouchListener(SetAction("forward"))
            btnHomeLeft.setOnTouchListener(SetAction("left"))
            btnHomeBackward.setOnTouchListener(SetAction("backward"))
            btnHomeRight.setOnTouchListener(SetAction("right"))
            btnHomeFlash.setOnClickListener {
                viewModel.isFlash.value?.let {
                    viewModel.setFlashState(!it)
                }
            }
        }
    }

    companion object {
        const val URL_KEY = "url_key"
    }

    inner class SetAction(private val direction: String) : View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(view: View?, event: MotionEvent?): Boolean {
            return when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewModel.setAction(direction)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    viewModel.setAction("stop")
                    true
                }
                else -> false
            }
        }
    }
}