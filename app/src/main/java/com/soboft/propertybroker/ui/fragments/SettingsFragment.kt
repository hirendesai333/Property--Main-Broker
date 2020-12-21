package com.soboft.propertybroker.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.soboft.propertybroker.R
import com.soboft.propertybroker.databinding.FragmentSettingsBinding
import com.soboft.propertybroker.ui.activities.*

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)

        binding.profile.setOnClickListener {
            Intent(activity, Profile::class.java).apply {
                startActivity(this)
            }
        }

        binding.notification.setOnClickListener {
            Intent(activity, Notification::class.java).apply {
                startActivity(this)
            }
        }

        binding.logout.setOnClickListener {
            Intent(activity, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(this)
            }
        }

        binding.documents.setOnClickListener {
            Intent(activity, Documents::class.java).apply {
                startActivity(this)
            }
        }

        binding.myCustomers.setOnClickListener {
            Intent(activity, MyCustomers::class.java).apply {
                startActivity(this)
            }
        }

        binding.myProperties.setOnClickListener {
            Intent(activity, MyProperties::class.java).apply {
                startActivity(this)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}