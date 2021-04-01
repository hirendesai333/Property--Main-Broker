package com.illopen.agent.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.illopen.agent.databinding.ActivityChangePasswordBinding
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.ui.fragments.SettingsFragment
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.agent.utils.ProgressDialog
import com.illopen.properybroker.utils.toast
import kotlinx.coroutines.*

class ChangePassword : AppCompatActivity() {

    private val TAG = "ChangePassword"

    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var progressDialog: ProgressDialog

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)

        binding.title.setOnClickListener {
            onBackPressed()
        }

        binding.updatePass.setOnClickListener {
            passwordDetails()
        }
    }

    private fun passwordDetails() {
        val currentPass = binding.currentPass.text.toString().trim()
        val newPass = binding.newPass.text.toString().trim()
        val confirmPass = binding.confirmPass.text.toString().trim()

        when {
            currentPass.isEmpty() -> {
                binding.currentPass.error = "Please Enter Current Password"
                binding.currentPass.requestFocus()
            }
            newPass.isEmpty() -> {
                binding.newPass.error = "Please Enter New Password"
                binding.newPass.requestFocus()
            }
            confirmPass.isEmpty() -> {
                binding.confirmPass.error = "Please Enter Confirm Password"
                binding.confirmPass.requestFocus()
            }
            else -> {
                changePassAPI(currentPass, newPass, confirmPass)
            }
        }
    }

    private fun changePassAPI(currentPass: String, newPass: String, confirmPass: String) {
        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Id"] = AppPreferences.getUserData(Params.UserId)
                map["OldPassword"] = currentPass
                map["NewPassword"] = newPass
                map["ConfirmPassword"] = confirmPass
                val response = ServiceApi.retrofitService.changePassword(
                    1,
                    map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        if (response.code() == 200) {

                            Log.d(TAG, response.code().toString())
                            Log.d(TAG, response.body().toString())

                            Toast.makeText(applicationContext,"Password Update Successfully",Toast.LENGTH_LONG).show()
//                            startActivity(Intent(this@ChangePassword,SettingsFragment::class.java))
                        } else {
                            toast("Please try again")
                        }
                        progressDialog.dialog.dismiss()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d("Password Not Updated", response.code().toString())
                        progressDialog.dialog.dismiss()
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                progressDialog.dialog.dismiss()
            }
        }
    }
}