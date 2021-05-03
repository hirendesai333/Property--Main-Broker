package com.illopen.agent.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.illopen.agent.adapters.OnGoingPropertyBidListAdapter
import com.illopen.agent.databinding.ActivityOnGoingPropertyBidListBinding
import com.illopen.agent.model.JobBidList
import com.illopen.agent.network.ServiceApi
import kotlinx.coroutines.*
import java.util.HashMap

class OnGoingPropertyBidList : AppCompatActivity() {

    private lateinit var binding: ActivityOnGoingPropertyBidListBinding

    private val TAG = "OnGoingJobPropertyBidList"

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    private lateinit var jobId : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnGoingPropertyBidListBinding.inflate(layoutInflater)
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

                        Log.d("JobPropertyBidList", response.code().toString())
                        Log.d("JobPropertyBidList", response.body().toString())

                        val list : List<JobBidList> = response.body()!!.values!!
                        if (list.isNotEmpty()) {
                            binding.ongoingPropertyList.adapter = OnGoingPropertyBidListAdapter(this@OnGoingPropertyBidList, list)
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
}