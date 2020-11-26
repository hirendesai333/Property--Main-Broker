package com.soboft.propertybroker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.soboft.propertybroker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener {
//            loadFragment(AddPropertyFragment())
            startActivity(Intent(this, AddProperty::class.java))
        }

        binding.bottomNavigationView.background = null
//        binding.bottomNavigationView.menu.getItem(2).isEnabled = false

    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        //switching fragment
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit()
            return true
        }
        return false
    }

}