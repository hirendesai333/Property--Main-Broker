package com.soboft.propertybroker.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.soboft.propertybroker.R
import com.soboft.propertybroker.adapters.JobPropertyAdapter
import com.soboft.propertybroker.adapters.OnGoingJobAdapter
import com.soboft.propertybroker.databinding.ActivityNewJobDetailsBinding
import com.soboft.propertybroker.databinding.ActivityOnGoingJobDetailsBinding
import com.soboft.propertybroker.listeners.OnGoingJobPropertyClick
import com.soboft.propertybroker.model.JobPropertyList
import com.soboft.propertybroker.network.ServiceApi
import kotlinx.coroutines.*
import java.util.HashMap

class OnGoingJobDetails : AppCompatActivity(), OnGoingJobPropertyClick {

    private lateinit var binding: ActivityOnGoingJobDetailsBinding

    private val TAG = "OnGoingJobDetails"

    private lateinit var jobId: String

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnGoingJobDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        jobId = intent.getStringExtra("OnGoingData")!!

        getOnGoingJobProperty()
    }

    private fun getOnGoingJobProperty() {
        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getJobProperty(jobId.toInt(),map)

                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getOnGoingJobProperty", response.code().toString())
                        Log.d("getOnGoingJobProperty", response.body().toString())

                        val list : List<JobPropertyList> = response.body()!!.values!!

                        binding.onGoingJobProperty.adapter = OnGoingJobAdapter(this@OnGoingJobDetails,list,this@OnGoingJobDetails)

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

    override fun onGoingJobPropertyClick(currentItem: JobPropertyList) {
        val intent =  Intent(this,PropertyDetail::class.java)
        startActivity(intent)
    }
}