package com.illopen.agent.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.illopen.agent.databinding.ActivityPropertyMoreDetailsBinding

class PropertyMoreDetails : AppCompatActivity() {
    private lateinit var binding: ActivityPropertyMoreDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyMoreDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backbtn.setOnClickListener {
            onBackPressed()
        }
    }
}