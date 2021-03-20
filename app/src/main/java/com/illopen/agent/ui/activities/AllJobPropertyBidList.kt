package com.illopen.agent.ui.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.adapters.AllCityListAdapter
import com.illopen.agent.adapters.JobPropertyBidAllAdapter
import com.illopen.agent.databinding.ActivityAllJobPropertyBidListBinding
import com.illopen.agent.model.*
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.properybroker.utils.toast
import kotlinx.coroutines.*
import java.util.HashMap

class AllJobPropertyBidList : AppCompatActivity() , JobPropertyBidAllAdapter.OnItemClickListener, JobPropertyBidAllAdapter.OnPropertyShowClick {
    private lateinit var binding: ActivityAllJobPropertyBidListBinding

    private val TAG = "AllJobPropertyBidList"

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    private lateinit var jobId : String

    private lateinit var agentPopupDialog : Dialog
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

                        val list : List<JobBidValue> = response.body()!!.values!!
                        if (list.isNotEmpty()) {
                            binding.allBidProperty.adapter =
                                JobPropertyBidAllAdapter(this@AllJobPropertyBidList, list,this@AllJobPropertyBidList,this@AllJobPropertyBidList)
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

    override fun onItemClick(itemPosition: Int, data: JobBidValue) {

        agentPopupDialog = Dialog(this, R.style.Theme_PropertyMainBroker)
        agentPopupDialog.setContentView(R.layout.single_bid_list_item)
        agentPopupDialog.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        val title = agentPopupDialog.findViewById<TextView>(R.id.title)
        val amount = agentPopupDialog.findViewById<TextView>(R.id.amount)


        title.text = data.propertyName.toString()
        amount.text = data.propertyPrice.toString()

        val acceptBtn = agentPopupDialog.findViewById<Button>(R.id.accept)
        val rejectBtn = agentPopupDialog.findViewById<Button>(R.id.reject)

        acceptBtn.setOnClickListener {
            agentPopupDialog.cancel()
            agentPopup(data)
            Log.d(TAG, "onItemClick: $data")
        }

        rejectBtn.setOnClickListener { agentPopupDialog.cancel() }

        agentPopupDialog.show()
    }

    private fun agentPopup(data: JobBidValue) {
        coroutineScope.launch {
            try {

                val response = ServiceApi.retrofitService.getJobAssignUserId(jobId.toInt(),data.id!!.toInt())
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("Assigned Agent Details", response.code().toString())
                        Log.d("Assigned Agent Details", response.body().toString())

                        toast("Job Assigned User Updated Successfully")
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

    override fun onEyePropertyClick(itemPosition: Int, data: JobBidValue) {

        agentShowProperty = Dialog(this, R.style.Theme_PropertyMainBroker)
        agentShowProperty.setContentView(R.layout.single_bid_list_item)
        agentShowProperty.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        val bidDetailsRv = agentShowProperty.findViewById<RecyclerView>(R.id.agentBidDetails)

        agentBidDetailsList(bidDetailsRv)

    }

    private fun agentBidDetailsList(bidDetailsRv: RecyclerView?) {

        coroutineScope.launch {
            try {
                val map = HashMap<String,String>()
                map["Offset"] =  "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getCity(map)
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getBidDetails", response.code().toString())
                        Log.d("getBidDetails",response.body().toString())

//                        val list : List<> = response.body()!!.values!!
//
//                        bidDetailsRv.adapter = AllCityListAdapter(this@AddProperty,list,this@AddProperty)
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