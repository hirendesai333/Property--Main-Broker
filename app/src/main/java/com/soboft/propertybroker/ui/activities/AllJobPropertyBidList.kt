package com.soboft.propertybroker.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.soboft.propertybroker.R
import com.soboft.propertybroker.adapters.JobPropertyAdapter
import com.soboft.propertybroker.adapters.JobPropertyBidAllAdapter
import com.soboft.propertybroker.databinding.ActivityAllJobPropertyBidListBinding
import com.soboft.propertybroker.databinding.ActivityNewJobDetailsBinding
import com.soboft.propertybroker.model.JobPropertyBidAllList
import com.soboft.propertybroker.model.JobPropertyList
import com.soboft.propertybroker.network.ServiceApi
import com.soboft.propertybroker.utils.AppPreferences
import com.soboft.propertybroker.utils.Params
import kotlinx.coroutines.*
import java.util.HashMap

class AllJobPropertyBidList : AppCompatActivity() {
    private lateinit var binding: ActivityAllJobPropertyBidListBinding

    private val TAG = "AllJobPropertyBidList"

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    private lateinit var jobId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllJobPropertyBidListBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

                val response = ServiceApi.retrofitService.getJobPropertyBidAll(
                    AppPreferences.getUserData(Params.UserId).toInt(), jobId.toInt(), map)

                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("allJobPropertyBidList", response.code().toString())
                        Log.d("allJobPropertyBidList", response.body().toString())

                        val list : List<JobPropertyBidAllList> = response.body()!!.values!!

                        binding.allBidProperty.adapter = JobPropertyBidAllAdapter(this@AllJobPropertyBidList,list)

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