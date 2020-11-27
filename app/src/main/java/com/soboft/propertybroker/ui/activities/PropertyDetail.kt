package com.soboft.propertybroker.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import androidx.appcompat.app.AppCompatActivity
import com.soboft.propertybroker.R
import com.soboft.propertybroker.databinding.ActivityPropertyDetailBinding
import com.soboft.propertybroker.databinding.FragmentUpcomingJobsBinding

class PropertyDetail : AppCompatActivity() {

    private lateinit var binding: ActivityPropertyDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.allBids.setOnClickListener {
            val intent = Intent(this, BidsList::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                Pair.create(binding.allBids, "all_bids"),
            )
            startActivity(intent, options.toBundle())
        }
    }

}