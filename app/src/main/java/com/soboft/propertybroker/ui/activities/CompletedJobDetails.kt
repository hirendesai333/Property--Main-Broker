package com.soboft.propertybroker.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.soboft.propertybroker.R
import com.soboft.propertybroker.adapters.CompletedJobDetailsAdapter
import com.soboft.propertybroker.adapters.OnGoingJobAdapter
import com.soboft.propertybroker.databinding.ActivityCompletedJobDetailsBinding
import com.soboft.propertybroker.databinding.ActivityOnGoingJobDetailsBinding
import com.soboft.propertybroker.listeners.OnCompletedJobClick
import com.soboft.propertybroker.listeners.OnCompletedJobPropertyClick
import com.soboft.propertybroker.model.JobPropertyList
import com.soboft.propertybroker.network.ServiceApi
import com.soboft.propertybroker.utils.AppPreferences
import com.soboft.propertybroker.utils.Params
import kotlinx.coroutines.*
import java.util.HashMap

class CompletedJobDetails : AppCompatActivity() , OnCompletedJobPropertyClick{

    private lateinit var binding: ActivityCompletedJobDetailsBinding

    private val TAG = "CompletedJobDetails"

    private lateinit var jobId: String

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompletedJobDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        jobId = intent.getStringExtra("CompletedJob")!!

        binding.allBidPropertyDetails.setOnClickListener {
            val intent =  Intent(this,AllJobPropertyBidList::class.java)
            intent.putExtra("job",jobId)
            startActivity(intent)
        }

        binding.title.setOnClickListener { onBackPressed() }

        getCompletedJob()
    }

    private fun getCompletedJob() {
        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getJobProperty(jobId.toInt(),AppPreferences.getUserData(Params.UserId).toInt(),map)

                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getCompletedJob", response.code().toString())
                        Log.d("getCompletedJob", response.body().toString())

                        val list : List<JobPropertyList> = response.body()!!.values!!

                        binding.completedJobProperty.adapter = CompletedJobDetailsAdapter(this@CompletedJobDetails,list,this@CompletedJobDetails)

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

    override fun onCompletedJobPropertyClick(currentItem: JobPropertyList) {
        val intent =  Intent(this,PropertyDetail::class.java)
        startActivity(intent)
    }
}