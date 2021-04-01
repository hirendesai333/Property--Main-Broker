package com.illopen.agent.ui.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.illopen.agent.R
import com.illopen.agent.adapters.MyPostedJobDetailsAdapter
import com.illopen.agent.databinding.ActivityMyPostedJobDetailsBinding
import com.illopen.agent.model.JobPropertyList
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.properybroker.utils.toast
import kotlinx.coroutines.*
import java.util.HashMap

class MyPostedJobDetails : AppCompatActivity() {

    private lateinit var binding: ActivityMyPostedJobDetailsBinding

    private val TAG = "MyPostedJobDetails"

    private lateinit var jobId: String
//    private lateinit var from : String

    private lateinit var agentPopupDialog : Dialog

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPostedJobDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        jobId = intent.getStringExtra("PostData")!!

        getMyPostedJobProperty()

        binding.addAllBid.setOnClickListener {
            val intent =  Intent(this,AllJobPropertyBidList::class.java)
            intent.putExtra("job",jobId)
            startActivity(intent)
        }
        binding.title.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getMyPostedJobProperty() {
        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getJobProperty(jobId.toInt(),
                    AppPreferences.getUserData(Params.UserId).toInt(), map)

                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getMyPostedJobProperty", response.code().toString())
                        Log.d("getMyPostedJobProperty", response.body().toString())

                        val list : List<JobPropertyList> = response.body()!!.values!!

                        if (list.isNotEmpty()){
                            binding.myPostedJobRv.adapter = MyPostedJobDetailsAdapter(this@MyPostedJobDetails,list)
                        }else{
                            //no property
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

//    override fun onItemClick(itemPosition: Int, data: JobPropertyList) {
//
//        agentPopupDialog = Dialog(this, R.style.Theme_PropertyMainBroker)
//        agentPopupDialog.setContentView(R.layout.single_bid_list_item)
//        agentPopupDialog.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)
//
//        val title = agentPopupDialog.findViewById<TextView>(R.id.title)
//        val amount = agentPopupDialog.findViewById<TextView>(R.id.amount)
//
//
//        title.text = data.propertyName.toString()
//        amount.text = data.propertyPrice.toString()
//
//        val acceptBtn = agentPopupDialog.findViewById<Button>(R.id.accept)
//        val rejectBtn = agentPopupDialog.findViewById<Button>(R.id.reject)
//
//        acceptBtn.setOnClickListener {
//            agentPopupDialog.cancel()
////            agentPopup(data)
//            Log.d(TAG, "onItemClick: "+ data.toString())
//        }
//
//        rejectBtn.setOnClickListener { agentPopupDialog.cancel() }
//
//        agentPopupDialog.show()
//    }

//    private fun agentPopup(data: JobPropertyList) {
//        coroutineScope.launch {
//            try {
//
//                val response = ServiceApi.retrofitService.getJobAssignUserId(jobId.toInt(),data.id!!.toInt())
//                if (response.isSuccessful) {
//                    withContext(Dispatchers.Main) {
//
//                        Log.d("Assigned Agent Details", response.code().toString())
//                        Log.d("Assigned Agent Details", response.body().toString())
//
//                        toast("Job Assigned User Updated Successfully")
//                    }
//                } else {
//                    withContext(Dispatchers.Main) {
//                        Log.d(TAG, "something wrong")
//                    }
//                }
//            } catch (e: Exception) {
//                Log.d(TAG, e.message.toString())
//            }
//        }
//    }
}