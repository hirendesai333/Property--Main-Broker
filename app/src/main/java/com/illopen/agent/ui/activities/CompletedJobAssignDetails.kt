package com.illopen.agent.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.illopen.agent.R
import com.illopen.agent.adapters.CompletedJobAssignDetailsAdapter
import com.illopen.agent.adapters.CompletedJobDetailsAdapter
import com.illopen.agent.databinding.ActivityCompletedJobAssignDetailsBinding
import com.illopen.agent.databinding.ActivityCompletedJobDetailsBinding
import com.illopen.agent.model.JobPropertyList
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import kotlinx.coroutines.*
import java.util.HashMap

class CompletedJobAssignDetails : AppCompatActivity() {

    private lateinit var binding: ActivityCompletedJobAssignDetailsBinding

    private val TAG = "CompletedJobAssignDetails"

    private lateinit var jobId: String
    private val statusMasterId : Int = 4

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompletedJobAssignDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        jobId = intent.getStringExtra("CompletedAssignJob")!!

//        binding.allBidPropertyDetails.setOnClickListener {
//            val intent =  Intent(this,AllJobPropertyBidList::class.java)
//            intent.putExtra("job",jobId)
//            startActivity(intent)
//        }

        binding.title.setOnClickListener { onBackPressed() }

        getCompletedJobAssign()
    }

    private fun getCompletedJobAssign() {
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

                        Log.d("getCompletedJob", response.code().toString())
                        Log.d("getCompletedJob", response.body().toString())

                        val list : List<JobPropertyList> = response.body()!!.values!!

                        binding.completedJobProperty.adapter = CompletedJobAssignDetailsAdapter(this@CompletedJobAssignDetails,list)
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