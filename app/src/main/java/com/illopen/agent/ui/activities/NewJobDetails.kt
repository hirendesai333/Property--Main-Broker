package com.illopen.agent.ui.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.illopen.agent.R
import com.illopen.agent.adapters.JobPropertyAdapter
import com.illopen.agent.databinding.ActivityNewJobDetailsBinding
import com.illopen.agent.listeners.OnJobPropertyClick
import com.illopen.agent.model.JobPropertyList
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.properybroker.utils.toast
import kotlinx.coroutines.*
import java.util.*


class NewJobDetails : AppCompatActivity(), OnJobPropertyClick {

    private lateinit var binding: ActivityNewJobDetailsBinding

    private val TAG = "NewJobDetails"

    private lateinit var jobId: String

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewJobDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        jobId = intent.getStringExtra("JobData")!!

        getJobProperty()

        binding.addAllBid.setOnClickListener {
            val intent = Intent(this, AllJobPropertyBidList::class.java)
            intent.putExtra("job", jobId)
            startActivity(intent)
        }

        binding.title.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getJobProperty() {

        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getJobProperty(
                    jobId.toInt(),
                    AppPreferences.getUserData(Params.UserId).toInt(),
                    map
                )

                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("getJobProperty", response.code().toString())
                        Log.d("getJobProperty", response.body().toString())

                        val list: List<JobPropertyList> = response.body()!!.values!!
                        if (list.isNotEmpty()) {
                            binding.jobProperty.adapter =
                                JobPropertyAdapter(this@NewJobDetails, list, this@NewJobDetails)
                        } else {
                            // no property found
                        }

                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "something wrong")
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
            }
        }
    }

    override fun onJobPropertyClick(currentItem: JobPropertyList) {
        showBidPopup(currentItem)
    }

    private fun showBidPopup(currentItem: JobPropertyList) {
        val view: View = LayoutInflater.from(this).inflate(R.layout.bid_layout_popup, null);
        val dialog = MaterialAlertDialogBuilder(
            this,
            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
            .setView(view)
            .setBackground(ColorDrawable(Color.TRANSPARENT))
            .create()

        val bidButton = view.findViewById<Button>(R.id.bidSubmit)
        val bidAmount = view.findViewById<EditText>(R.id.bidAmount)
        val description = view.findViewById<EditText>(R.id.description)

        if (currentItem.bidAmount!! > 0) {
            bidAmount.setText(currentItem.bidAmount.toString())
            description.setText(currentItem.bidNote)
            bidButton.text = "Update Bid"
        }

        bidButton.setOnClickListener {
            val amount = bidAmount.text.toString().trim()
            val desc = description.text.toString().trim()

            if (amount.isNotEmpty()) {
                bidingData(amount, desc, currentItem)
                dialog.cancel()
            } else {
                toast("Please enter bid amount")
            }
        }

        dialog.show()
    }

    private fun bidingData(bidAmount: String, description: String, currentItem: JobPropertyList) {
        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Id"] = currentItem.jobPropertyBidId.toString()
                map["JobPropertyId"] = currentItem.id.toString()
                map["UserId"] = AppPreferences.getUserData(Params.UserId)
                map["Amount"] = bidAmount
                map["Note"] = description
                val response = ServiceApi.retrofitService.jobPropertyBid(map)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        if (response.code() == 200) {
                            toast(response.body()!!.message!!)
                            getJobProperty()
                        } else {
                            toast("Please try again")
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "something wrong")
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
            }
        }
    }
}