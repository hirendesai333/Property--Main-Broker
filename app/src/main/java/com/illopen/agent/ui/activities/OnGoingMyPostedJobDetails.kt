package com.illopen.agent.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.illopen.agent.adapters.OnGoingMyPostedJobsAdapter
import com.illopen.agent.databinding.ActivityOnGoingMyPostedJobDetailsBinding
import com.illopen.agent.model.JobPropertyList
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import kotlinx.coroutines.*
import java.util.HashMap

class OnGoingMyPostedJobDetails : AppCompatActivity() {

    private lateinit var binding: ActivityOnGoingMyPostedJobDetailsBinding

    private val TAG = "OnGoing_MyPosted_Job_Details"

    private lateinit var jobId: String

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnGoingMyPostedJobDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        jobId = intent.getStringExtra("OnGoingMyPostedData")!!

        getOnGoingMyPostedJobProperty()

        binding.allBidDetails.setOnClickListener {
            val intent =  Intent(this,OnGoingPropertyBidList::class.java)
            intent.putExtra("job",jobId)
            startActivity(intent)
        }

        binding.title.setOnClickListener { onBackPressed() }
    }

    private fun getOnGoingMyPostedJobProperty() {
        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getJobProperty(jobId.toInt(),
                    AppPreferences.getUserData(Params.UserId).toInt(),map)

                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getMyPostedJobProperty", response.code().toString())
                        Log.d("getMyPostedJobProperty", response.body().toString())

                        val list : List<JobPropertyList> = response.body()!!.values!!

                        binding.ongoingMyPostedJobRv.adapter = OnGoingMyPostedJobsAdapter(this@OnGoingMyPostedJobDetails,list)

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