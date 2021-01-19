package com.soboft.propertybroker.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.soboft.propertybroker.databinding.ActivityLoginBinding
import com.soboft.propertybroker.network.ServiceApi
import com.soboft.properybroker.utils.toast
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val TAG = "LoginActivity"

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        if (email.isNullOrEmpty() && pass.isNullOrEmpty())
        {
            toast("please enter email & Password")
        }else{
//                Intent(this, MainActivity::class.java).apply {
//                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                    startActivity(this)
            coroutineScope.launch {
                try {
                    val response = ServiceApi.retrofitService.login(
                        email,
                        pass
                    )
                    if (response.isSuccessful){
                        withContext(Dispatchers.Main){
                            Log.d("login", response.code().toString())
                            Log.d("login",response.body().toString())

                            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                        }
                    }else{
                        withContext(Dispatchers.Main){
                            Log.d("login fail", response.code().toString())
                        }
                    }
                }catch (e : Exception){
                    Log.d(TAG, e.message.toString())
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