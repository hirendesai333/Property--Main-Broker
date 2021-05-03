package com.illopen.agent.ui.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.illopen.agent.R
import com.illopen.agent.adapters.AgentBidPropertyListAdapter
import com.illopen.agent.adapters.JobPropertyBidAllAdapter
import com.illopen.agent.databinding.ActivityAllJobPropertyBidListBinding
import com.illopen.agent.model.*
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.ui.fragments.AllPropertyListFragment
import com.illopen.agent.ui.fragments.UpcomingJobsFragment
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.properybroker.utils.toast
import kotlinx.coroutines.*
import java.util.HashMap

class AllJobPropertyBidList : AppCompatActivity() , JobPropertyBidAllAdapter.OnItemClickListener {
    private lateinit var binding: ActivityAllJobPropertyBidListBinding

    private val TAG = "AllJobPropertyBidList"

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    private lateinit var jobId : String

    private lateinit var agentShowProperty : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllJobPropertyBidListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.title.setOnClickListener {
            onBackPressed()
        }

        jobId = intent.getStringExtra("job")!!

        allJobPropertyBid()
    }

    private fun allJobPropertyBid() {
        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"
                val response = ServiceApi.retrofitService.getJobAllBids(jobId.toInt(), map)

                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("allJobPropertyBidList", response.code().toString())
                        Log.d("allJobPropertyBidList", response.body().toString())

                        val list : List<JobBidList> = response.body()!!.values!!
                        if (list.isNotEmpty()) {
                            binding.allBidProperty.adapter =
                                JobPropertyBidAllAdapter(this@AllJobPropertyBidList, list,
                                    this@AllJobPropertyBidList)
                        } else {
                            // no bids found
                        }

                    }
                }else{
                    withContext(Dispatchers.Main){
                        Log.d(TAG, "something wrong")
                    }
                }
            }catch (e : Exception){
                Log.d(TAG, e.message.toString())
            }
        }
    }

    override fun onAssignClick(itemPosition: Int, data: JobBidList) {

        val agentPopupDialog = Dialog(this, R.style.Theme_PropertyMainBroker)
        agentPopupDialog.setContentView(R.layout.single_bid_list_item)
        agentPopupDialog.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        val title = agentPopupDialog.findViewById<TextView>(R.id.title)
        val amount = agentPopupDialog.findViewById<TextView>(R.id.amount)


        title.text = data.userName.toString()
        amount.text = "Total Amount: "+ data.totalAmount.toString()

        val acceptBtn = agentPopupDialog.findViewById<Button>(R.id.accept)
        val rejectBtn = agentPopupDialog.findViewById<Button>(R.id.reject)

        acceptBtn.setOnClickListener {
            agentPopupDialog.cancel()
            agentPopup(data)
            Log.d(TAG, "onItemClick: $data")
        }

        rejectBtn.setOnClickListener {
            agentPopupDialog.cancel()
        }

        agentPopupDialog.show()
    }

    private fun agentPopup(data: JobBidList) {
        coroutineScope.launch {
            try {

                val response = ServiceApi.retrofitService.getJobAssignUserId(jobId.toInt(),data.userId!!.toInt())
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("Assigned job", response.code().toString())
                        Log.d("Assigned job", response.body().toString())

                        if (response.code() == 200) {
                            toast("Job Assigned Successfully")
                        }else{
                            toast("Job Not Assigned")
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

    override fun onEyePropertyClick(itemPosition: Int, data: JobBidList) {

        agentShowProperty = Dialog(this, R.style.Theme_PropertyMainBroker)
        agentShowProperty.setContentView(R.layout.bid_details_popup)
        agentShowProperty.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        val bidDetailsRv = agentShowProperty.findViewById<RecyclerView>(R.id.agentBidDetails)
        agentBidDetailsList(bidDetailsRv,data)
        agentShowProperty.show()
    }

    private fun agentBidDetailsList(bidDetailsRv: RecyclerView?,data: JobBidList) {
        coroutineScope.launch {
            try {
                val map = HashMap<String,String>()
                map["Offset"] =  "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getJobProperty(jobId.toInt(),
                    data.userId!!.toInt(),map)
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getBidDetails", response.code().toString())
                        Log.d("getBidDetails",response.body().toString())

                        val list : List<JobPropertyList> = response.body()!!.values!!
                        bidDetailsRv!!.adapter = AgentBidPropertyListAdapter(this@AllJobPropertyBidList,list)
                    }
                }else{
                    withContext(Dispatchers.Main){
                        Log.d(TAG, "something wrong")
                    }
                }
            }catch (e : Exception){
                Log.d(TAG, e.message.toString())
            }
        }

    }
}