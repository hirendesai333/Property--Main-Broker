package com.illopen.agent.ui.activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.illopen.agent.adapters.CompletedJobDetailsAdapter
import com.illopen.agent.databinding.ActivityCompletedJobDetailsBinding
import com.illopen.agent.model.JobPropertyList
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.properybroker.utils.toast
import kotlinx.coroutines.*
import java.util.HashMap

class CompletedJobDetails : AppCompatActivity() , CompletedJobDetailsAdapter.OnCompletedJobPropertyClick{

    private lateinit var binding: ActivityCompletedJobDetailsBinding

    private val TAG = "CompletedJobDetails"

    private lateinit var jobId: String
    private val statusMasterId : Int = 4

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
        markPropertyAsCompleted(currentItem)
    }

    private fun markPropertyAsCompleted(currentItem: JobPropertyList) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are You Sure Mark As Completed!")
        builder.setPositiveButton("YES") { dialogInterface: DialogInterface, i: Int ->
            completedMarked(currentItem)
        }
        builder.setNegativeButton("NO") { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
        }
        builder.show()
    }

    private fun completedMarked(currentItem: JobPropertyList) {
        coroutineScope.launch {
            try {
                val response = ServiceApi.retrofitService.markJobPropertyStatus(
                    jobId.toInt(),
                    statusMasterId
                )
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("Mark Completed Property", response.code().toString())
                        Log.d("Mark CompletedProperty", response.body().toString())

                        toast("Complete Property  Marked  Successfully")
                    }
                }else{
                    withContext(Dispatchers.Main){
                        Log.d(TAG, "something wrong ")
                    }
                }
            }catch (e : Exception){
                Log.d(TAG, e.message.toString())
            }
        }
    }
}