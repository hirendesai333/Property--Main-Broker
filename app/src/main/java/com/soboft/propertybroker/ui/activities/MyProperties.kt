package com.soboft.propertybroker.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.soboft.propertybroker.adapters.MyPropertiesAdapter
import com.soboft.propertybroker.databinding.ActivityMyPropertiesBinding

class MyProperties : AppCompatActivity() {

    lateinit var binding: ActivityMyPropertiesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPropertiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.title.setOnClickListener {
            onBackPressed()
        }

        binding.myPropertiesRv.adapter = MyPropertiesAdapter()
    }
}