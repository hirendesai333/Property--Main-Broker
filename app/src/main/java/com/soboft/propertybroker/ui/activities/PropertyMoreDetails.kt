package com.soboft.propertybroker.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.soboft.propertybroker.R
import com.soboft.propertybroker.databinding.ActivityAddPropertyBinding
import com.soboft.propertybroker.databinding.ActivityPropertyMoreDetailsBinding

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