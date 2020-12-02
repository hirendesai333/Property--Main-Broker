package com.soboft.propertybroker.ui.activities

import android.app.ActivityOptions
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.soboft.propertybroker.R
import com.soboft.propertybroker.databinding.ActivityPropertyDetailBinding
import com.soboft.propertybroker.utils.Params
import java.util.*

class PropertyDetail : AppCompatActivity() {

    private lateinit var binding: ActivityPropertyDetailBinding
    private var parentActivity: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        parentActivity = parentActivity.let {
            intent.getStringExtra(Params.FROM)!!
        }

        when (parentActivity) {
            Params.ALL_PROPERTY_LIST_FRAGMENT -> {
                binding.newJob.visibility = View.GONE
                binding.bidLayout.visibility = View.GONE
                binding.assignedToLayout.visibility = View.VISIBLE
            }
            Params.ONGOING_JOBS_FRAGMENT -> {
                binding.newJob.visibility = View.GONE
                binding.bidLayout.visibility = View.VISIBLE
                binding.assignedToLayout.visibility = View.GONE
            }
            else -> {
                binding.newJob.visibility = View.VISIBLE
                binding.bidLayout.visibility = View.VISIBLE
                binding.assignedToLayout.visibility = View.GONE
            }
        }

        binding.allBids.setOnClickListener {
            when (parentActivity) {
                Params.ONGOING_JOBS_FRAGMENT -> {
                    val intent = Intent(this, BidsList::class.java)
                    val options = ActivityOptions.makeSceneTransitionAnimation(
                        this,
                        Pair.create(binding.allBids, "all_bids"),
                    )
                    startActivity(intent, options.toBundle())
                }
                Params.COMPLETED_JOBS_FRAGMENT -> {
                    val intent = Intent(this, AgentListCompletedActivity::class.java)
                    val options = ActivityOptions.makeSceneTransitionAnimation(
                        this,
                        Pair.create(binding.allBids, "all_bids"),
                    )
                    startActivity(intent, options.toBundle())
                }
            }
        }

        binding.newJob.setOnClickListener {
            showNewJobPopup()
        }

    }

    private fun showNewJobPopup() {
        val mDialog = Dialog(this, R.style.Theme_PropertyMainBroker)
        mDialog.setContentView(R.layout.new_job_timing_popup)

        val selectTime = mDialog.findViewById<TextView>(R.id.selectTime)
        val selectDate = mDialog.findViewById<TextView>(R.id.selectDate)
        val createJob = mDialog.findViewById<TextView>(R.id.createJob)

        selectTime.setOnClickListener {
            val materialTimePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build()
            materialTimePicker.show(supportFragmentManager, "fragment_tag")
        }

        selectDate.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val picker = builder.build()
            val now = Calendar.getInstance()
            builder.setSelection(now.timeInMillis)
            picker.show(supportFragmentManager, picker.toString())
        }

        createJob.setOnClickListener {
            mDialog.dismiss()
        }

        mDialog.show()
    }

}