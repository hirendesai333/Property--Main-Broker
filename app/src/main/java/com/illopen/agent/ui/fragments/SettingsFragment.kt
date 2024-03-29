package com.illopen.agent.ui.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.illopen.agent.R
import com.illopen.agent.databinding.FragmentSettingsBinding
import com.illopen.agent.ui.activities.*
import com.illopen.agent.utils.AppPreferences

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)

        AppPreferences.initialize(this.requireContext())

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
            val builder = activity?.let { it1 -> MaterialAlertDialogBuilder(it1) }
            builder?.setTitle("LogOut")
            builder?.setMessage("Are you sure you want to LogOut!!")
            builder?.setPositiveButton("YES") { dialogInterface: DialogInterface, i: Int ->
                Intent(activity, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    AppPreferences.logOut()
                    startActivity(this)
                }
                dialogInterface.dismiss()
            }
            builder?.setNegativeButton("NO") { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
            builder?.show()
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

//        binding.dashBoard.setOnClickListener {
//            Intent(activity, Dashboard::class.java).apply {
//                startActivity(this)
//            }
//        }

        binding.changePass.setOnClickListener {
            Intent(activity, ChangePassword::class.java).apply {
                startActivity(this)
            }
        }

        binding.Location.setOnClickListener {
           Intent(activity, ProfileMapActivity::class.java).apply {
               startActivity(this)
           }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}