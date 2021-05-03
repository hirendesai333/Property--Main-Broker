package com.illopen.agent.ui.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.adapters.AgentBidPropertyListAdapter
import com.illopen.agent.adapters.CompletedMyPostedJobBidAdapter
import com.illopen.agent.databinding.ActivityCompletedJobBidListBinding
import com.illopen.agent.model.JobBidList
import com.illopen.agent.model.JobPropertyList
import com.illopen.agent.network.ServiceApi
import kotlinx.coroutines.*
import java.util.HashMap

class CompletedJobBidList : AppCompatActivity(),
    CompletedMyPostedJobBidAdapter.OnPropertyShowClick {

    private lateinit var binding: ActivityCompletedJobBidListBinding

    private val TAG = "CompletedJobBidList"

    private lateinit var agentShowProperty: Dialog

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    private lateinit var jobId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompletedJobBidListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.title.setOnClickListener {
            onBackPressed()
        }

        jobId = intent.getStringExtra("job")!!

        completedJobPropertyBid()
    }

    private fun completedJobPropertyBid() {
        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"
                val response = ServiceApi.retrofitService.getJobAllBids(jobId.toInt(), map)

                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("allJobPropertyBidList", response.code().toString())
                        Log.d("allJobPropertyBidList", response.body().toString())

                        val list: List<JobBidList> = response.body()!!.values!!
                        binding.completedBidProperty.adapter =
                            CompletedMyPostedJobBidAdapter(
                                this@CompletedJobBidList,
                                list,
                                this@CompletedJobBidList
                            )

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
        completedBidDetailsList(bidDetailsRv, data)

        agentShowProperty.show()
    }

    private fun completedBidDetailsList(bidDetailsRv: RecyclerView?, data: JobBidList) {

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
                    data.userId!!.toInt(), map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("getBidDetails", response.code().toString())
                        Log.d("getBidDetails", response.body().toString())

                        val list: List<JobPropertyList> = response.body()!!.values!!

                        bidDetailsRv!!.adapter =
                            AgentBidPropertyListAdapter(this@CompletedJobBidList, list)
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