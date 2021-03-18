package com.illopen.agent.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.illopen.agent.databinding.ActivityLoginBinding
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.properybroker.utils.toast
import kotlinx.coroutines.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val TAG = "LoginActivity"

    var regex = "[A-Z0-9a-z]+([._%+-][A-Z0-9a-z]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    var pattern : Pattern = Pattern.compile(regex)

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppPreferences.initialize(this.applicationContext)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        initializeViews()

        binding.loginButton.setOnClickListener {
            loginDetails()
        }

        binding.register.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }
    }

    private fun loginDetails() {

        val email = binding.emailID.text.toString()
        val pass = binding.password.text.toString()

        if (email.isEmpty()) {
            binding.emailID.error = "Please Enter Email"
            binding.emailID.requestFocus()
        }else if (!pattern.matcher(email).matches()){
            binding.emailID.error = "Invalid Email"
            binding.emailID.requestFocus()
        }else if (pass.isEmpty()){
            binding.password.error = "Please Enter Password"
            binding.password.requestFocus()
//        }else if (pass.length >= 8){
//            binding.password.error = "Password Should be 8 or more Character"
//            binding.password.requestFocus()
//        }
        }else {

            coroutineScope.launch {
                try {
                    val response = ServiceApi.retrofitService.login(
                        email,
                        pass
                    )
                    if (response.isSuccessful) {
                        withContext(Dispatchers.Main) {
                            Log.d("login", response.code().toString())
                            Log.d("login", response.body().toString())

                            toast("Login Success")

                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))

                            AppPreferences.setUserData(Params.UserId,response.body()!!.item!!.id.toString())
                            AppPreferences.setUserData(Params.UserTypeMasterId,response.body()!!.item!!.userTypeMasterId.toString())
                            AppPreferences.setUserData(Params.Username,response.body()!!.item!!.userTypeName.toString())
                            AppPreferences.setUserData(Params.Email,response.body()!!.item!!.email.toString())
                            AppPreferences.setUserData(Params.MobileNumber,response.body()!!.item!!.phoneNumber.toString())
                            AppPreferences.setUserData(Params.ProfileUrl,response.body()!!.item!!.profileUrl.toString())
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Log.d("login fail", response.code().toString())
                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG, e.message.toString())
                    toast("Please Enter Valid Email & Password")
                }
            }
        }
    }

    private fun initializeViews() {
        binding.loginButton.apply {
            translationY = 300F
            alpha = 0F
            animate().translationY(0F).alpha(1F).setDuration(800).setStartDelay(400).start()
        }

        binding.loginCard.apply {
            translationX = 800F
            alpha = 0F
            animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(300).start()
        }

        binding.verticalView.apply {
            translationZ = 800F
            alpha = 0F
            animate().translationZ(0F).alpha(1F).setDuration(800).setStartDelay(300).start()
        }
    }

    override fun onBackPressed() {
        ActivityCompat.finishAffinity(this)
    }
}