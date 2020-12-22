package com.soboft.propertybroker.ui.activities

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.soboft.propertybroker.R
import com.soboft.propertybroker.databinding.ActivityAddPropertyBinding

class AddProperty : AppCompatActivity() {
    private lateinit var binding: ActivityAddPropertyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.title.setOnClickListener {
            onBackPressed()
        }

        binding.activeButton.setOnClickListener {
            binding.activeButton.background =
                ContextCompat.getDrawable(this, R.drawable.rounded_users_tabbar)
            binding.inactivateButton.setBackgroundColor(Color.TRANSPARENT)

        }

        binding.inactivateButton.setOnClickListener {
            binding.activeButton.setBackgroundColor(Color.TRANSPARENT)
            binding.inactivateButton.background =
                ContextCompat.getDrawable(this, R.drawable.rounded_users_tabbar)
        }

    }
}