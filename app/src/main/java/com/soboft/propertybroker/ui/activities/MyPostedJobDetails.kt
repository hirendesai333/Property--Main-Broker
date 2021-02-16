package com.soboft.propertybroker.ui.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.soboft.propertybroker.R
import com.soboft.propertybroker.adapters.JobPropertyAdapter
import com.soboft.propertybroker.adapters.MyPostedJobDetailsAdapter
import com.soboft.propertybroker.databinding.ActivityMyPostedJobDetailsBinding
import com.soboft.propertybroker.model.JobPropertyList
import com.soboft.propertybroker.network.ServiceApi
import com.soboft.propertybroker.utils.AppPreferences
import com.soboft.propertybroker.utils.Params
import com.soboft.properybroker.utils.toast
import kotlinx.coroutines.*
import java.util.HashMap

class MyPostedJobDetails : AppCompatActivity(), MyPostedJobDetailsAdapter.OnItemClickListener {

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
                    AppPreferences.getUserData(Params.UserId).toInt(),
                    map)

                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getMyPostedJobProperty", response.code().toString())
                        Log.d("getMyPostedJobProperty", response.body().toString())

                        val list : List<JobPropertyList> = response.body()!!.values!!

                        binding.myPostedJobRv.adapter = MyPostedJobDetailsAdapter(this@MyPostedJobDetails,list,this@MyPostedJobDetails)
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

    override fun onItemClick(itemPosition: Int, data: JobPropertyList) {

        agentPopupDialog = Dialog(this, R.style.Theme_PropertyMainBroker)
        agentPopupDialog.setContentView(R.layout.single_bid_list_item)
        agentPopupDialog.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        val acceptBtn = agentPopupDialog.findViewById<Button>(R.id.accept)
        val rejectBtn = agentPopupDialog.findViewById<Button>(R.id.reject)

        acceptBtn.setOnClickListener {
            agentPopupDialog.cancel()
            agentPopup(data)
        }

        rejectBtn.setOnClickListener { agentPopupDialog.cancel() }

        agentPopupDialog.show()
    }

    private fun agentPopup(data: JobPropertyList) {
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
}