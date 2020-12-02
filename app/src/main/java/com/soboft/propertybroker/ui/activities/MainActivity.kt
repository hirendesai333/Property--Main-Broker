package com.soboft.propertybroker.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.soboft.propertybroker.R
import com.soboft.propertybroker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        binding.bottomNav.setupWithNavController(navController)
        binding.bottomNav.getOrCreateBadge(R.id.settings).number = 2
        binding.bottomNav.background = null

        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddProperty::class.java))
        }

    }

    override fun onBackPressed() {
        ActivityCompat.finishAffinity(this)
    }

}