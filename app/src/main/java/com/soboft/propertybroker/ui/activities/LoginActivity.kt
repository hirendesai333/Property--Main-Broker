package com.soboft.propertybroker.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.soboft.propertybroker.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews()

        binding.loginButton.setOnClickListener {
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(this)
            }
        }

        binding.register.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
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