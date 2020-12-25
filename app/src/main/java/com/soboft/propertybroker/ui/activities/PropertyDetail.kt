package com.soboft.propertybroker.ui.activities

import android.app.ActivityOptions
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.soboft.propertybroker.R
import com.soboft.propertybroker.databinding.ActivityPropertyDetailBinding
import com.soboft.propertybroker.model.PropertyListModel
import com.soboft.propertybroker.utils.Params
import java.util.*

class PropertyDetail : AppCompatActivity() {

    private val TAG = "PropertyDetail"
    private lateinit var binding: ActivityPropertyDetailBinding
    private var parentActivity: String? = null
    private var subParent: String? = null
    private var jobData: PropertyListModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.title.setOnClickListener {
            onBackPressed()
        }

        parentActivity = parentActivity.let {
            intent.getStringExtra(Params.FROM)!!
        }

        jobData = jobData.let {
            intent.getParcelableExtra<PropertyListModel>("JobData")!!
        }

        subParent = subParent.let {
            intent.getStringExtra(Params.SUB_FROM)
        }

        when (parentActivity) {
            Params.ALL_PROPERTY_LIST_FRAGMENT -> {
                if (subParent == Params.OTHER_NEW_JOBS) {
                    binding.newJob.visibility = View.GONE
                    binding.bid.visibility = View.VISIBLE
                    binding.bidLayout.visibility = View.VISIBLE
                    binding.assignedToLayout.visibility = View.GONE
                    if (jobData!!.isBidAdded == 1) {
                        binding.bidAmountLayout.visibility = View.VISIBLE
                        binding.bid.text = "Edit Bid"
                    } else {
                        binding.bidAmountLayout.visibility = View.GONE
                        binding.bid.text = "Bid"
                    }
                } else {
                    binding.newJob.visibility = View.GONE
                    binding.bid.visibility = View.GONE
                    binding.bidLayout.visibility = View.VISIBLE
                    binding.assignedToLayout.visibility = View.GONE
                    binding.bidAmountLayout.visibility = View.GONE
                }
            }
            Params.ONGOING_JOBS_FRAGMENT -> {
                if (subParent == Params.JOB_ASSIGN_TO_ME) {
                    binding.newJob.visibility = View.VISIBLE
                    binding.bid.visibility = View.GONE
                    binding.bidLayout.visibility = View.GONE
                    binding.assignedToLayout.visibility = View.GONE
                    binding.bidAmountLayout.visibility = View.VISIBLE
                    if (jobData!!.jobStatus == 0) {
                        binding.newJob.text = "Start Job"
                    } else if (jobData!!.jobStatus == 1) {
                        binding.newJob.text = "Started"
                    }
                } else {
                    binding.newJob.visibility = View.VISIBLE
                    binding.bid.visibility = View.GONE
                    binding.bidLayout.visibility = View.GONE
                    binding.assignedToLayout.visibility = View.VISIBLE
                    binding.bidAmountLayout.visibility = View.VISIBLE
                    if (jobData!!.jobStatus == 0) {
                        binding.newJob.text = "Yet To Start"
                    } else if (jobData!!.jobStatus == 1) {
                        binding.newJob.text = "Started"
                    }
                }
            }
            else -> {
                binding.ratingBar.visibility = View.VISIBLE
                binding.totalBids.text = "Total Jobs: 48"
                binding.allBids.text = "ASSIGNED AGENTS"
                if (subParent == Params.MY_COMPLETED_JOBS) {
                    binding.newJob.visibility = View.GONE
                    binding.bid.visibility = View.GONE
                    binding.bidLayout.visibility = View.GONE
                    binding.assignedToLayout.visibility = View.GONE
                    binding.bidAmountLayout.visibility = View.VISIBLE
                } else {
                    binding.newJob.visibility = View.GONE
                    binding.bid.visibility = View.GONE
                    binding.bidLayout.visibility = View.VISIBLE
                    binding.assignedToLayout.visibility = View.VISIBLE
                    binding.bidAmountLayout.visibility = View.VISIBLE
                }
            }
        }

        binding.bid.setOnClickListener {
            showBidPopup()
        }

        binding.allBids.setOnClickListener {
            when (parentActivity) {
                Params.ALL_PROPERTY_LIST_FRAGMENT -> {
                    val intent = Intent(this, BidsList::class.java)
                    intent.putExtra(Params.SUB_FROM, subParent)
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
            when (parentActivity) {
                Params.ONGOING_JOBS_FRAGMENT -> {
                    if (subParent == Params.JOB_ASSIGN_TO_ME) {
                        if (jobData!!.jobStatus == 0) {
                            jobData!!.jobStatus = 1
                            binding.newJob.text = "Started"
                        } else if (jobData!!.jobStatus == 1) {
                            jobData!!.jobStatus = 2
                            binding.newJob.text = "Completed"
                        }
                    }
                }
            }
        }

    }

    private fun showBidPopup() {
        val view: View = LayoutInflater.from(this)
            .inflate(R.layout.bid_layout, null);

        val dialog = MaterialAlertDialogBuilder(
            this,
            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog
        )
            .setView(view)
            .setBackground(ColorDrawable(Color.TRANSPARENT))
            .create()

        val bidButton: Button = view.findViewById(R.id.bid)
        val bidAmount: EditText = view.findViewById(R.id.bidAmount)
        val description: EditText = view.findViewById(R.id.description)

        if (jobData!!.isBidAdded == 1) {
            bidAmount.setText("1500")
        } else {
            bidAmount.setText("")
        }

        bidButton.setOnClickListener {
            binding.bidAmountLayout.visibility = View.VISIBLE
            binding.bid.text = "Edit Bid"
            binding.bidOriginalAmount.text = "Bid amount: $${bidAmount.text.toString().trim()}"
            dialog.cancel()
        }

        dialog.show()
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