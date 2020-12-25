package com.soboft.propertybroker.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.soboft.propertybroker.databinding.ActivityProfileBinding
import com.soboft.properybroker.utils.toast

class Profile : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.title.setOnClickListener {
            onBackPressed()
        }

        binding.save.setOnClickListener {
            toast("Success")
        }

    }
}