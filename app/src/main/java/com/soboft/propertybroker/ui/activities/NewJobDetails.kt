package com.soboft.propertybroker.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.soboft.propertybroker.R
import com.soboft.propertybroker.adapters.JobPropertyAdapter
import com.soboft.propertybroker.databinding.ActivityNewJobDetailsBinding
import com.soboft.propertybroker.databinding.ActivityProfileBinding
import com.soboft.propertybroker.listeners.OnJobPropertyClick
import com.soboft.propertybroker.model.JobPropertyList
import com.soboft.propertybroker.network.ServiceApi
import com.soboft.propertybroker.utils.Params
import kotlinx.coroutines.*
import java.util.HashMap


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

                val response = ServiceApi.retrofitService.getJobProperty(jobId.toInt(),map)

                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getJobProperty", response.code().toString())
                        Log.d("getJobProperty", response.body().toString())

                        val list : List<JobPropertyList> = response.body()!!.values!!

                        binding.jobProperty.adapter = JobPropertyAdapter(this@NewJobDetails,list,this@NewJobDetails)

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

    override fun onJobPropertyClick(currentItem: JobPropertyList) {
//        Intent(this, PropertyDetail::class.java).apply {
//            putExtra("JobProperty", currentItem.id.toString())
//            startActivity(this)
//        }
        val intent =  Intent(this,PropertyDetail::class.java)
        startActivity(intent)
    }
}