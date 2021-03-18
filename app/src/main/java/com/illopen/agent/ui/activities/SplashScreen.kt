package com.illopen.agent.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import android.view.WindowManager
import com.illopen.agent.databinding.ActivitySplashScreenBinding
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import kotlinx.coroutines.*

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch {
            delay(2000L)
            withContext(Dispatchers.Main) {
                if (AppPreferences.getUserData(Params.UserId).isNullOrEmpty()){

                    val intent = Intent(this@SplashScreen, LoginActivity::class.java)
                    val options = ActivityOptions.makeSceneTransitionAnimation(
                        this@SplashScreen,
                        Pair.create(binding.logo, "app_logo"),
                        Pair.create(binding.backgroundImage, "background_image")
                    )
                    startActivity(intent, options.toBundle())

                } else{

                    val intent = Intent(this@SplashScreen,MainActivity::class.java)
                    startActivity(intent)
                }

            }
        }
    }
}