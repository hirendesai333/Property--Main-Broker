package com.illopen.agent.ui.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.illopen.agent.R
import com.illopen.agent.databinding.ActivityPropertyMoreDetailsBinding
import com.illopen.agent.model.PropertyMoreDetailsList
import com.illopen.agent.model.PropertyMoreDetailsTypeList
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.ui.fragments.PropertyDetailsFragment
import com.illopen.agent.ui.fragments.PropertyImageFragment
import com.illopen.properybroker.utils.toast
import kotlinx.coroutines.*

class PropertyMoreDetails : AppCompatActivity() {

    private lateinit var binding: ActivityPropertyMoreDetailsBinding

    private val TAG = "PropertyMoreDetails"

    lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyMoreDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(PropertyDetailsFragment())

        binding.propertyDetails.setOnClickListener {
            fragment = PropertyDetailsFragment()
            loadFragment(fragment)
            binding.propertyDetails.background =
                ContextCompat.getDrawable(this, R.drawable.rounded_users_tabbar)
            binding.imageUpload.setBackgroundColor(Color.TRANSPARENT)
        }

        binding.imageUpload.setOnClickListener {
            fragment = PropertyImageFragment()
            loadFragment(fragment)
            binding.imageUpload.background =
                ContextCompat.getDrawable(this, R.drawable.rounded_users_tabbar)
            binding.propertyDetails.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }
}