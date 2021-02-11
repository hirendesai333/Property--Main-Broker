package com.soboft.propertybroker.ui.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.soboft.propertybroker.R
import com.soboft.propertybroker.adapters.JobPropertyAdapter
import com.soboft.propertybroker.adapters.OnGoingJobAdapter
import com.soboft.propertybroker.databinding.ActivityNewJobDetailsBinding
import com.soboft.propertybroker.databinding.ActivityOnGoingJobDetailsBinding
import com.soboft.propertybroker.listeners.OnGoingJobPropertyClick
import com.soboft.propertybroker.model.JobPropertyList
import com.soboft.propertybroker.network.ServiceApi
import com.soboft.propertybroker.utils.AppPreferences
import com.soboft.propertybroker.utils.Params
import com.soboft.properybroker.utils.toast
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

        binding.allJobBid.setOnClickListener {
            val intent =  Intent(this,AllJobPropertyBidList::class.java)
            intent.putExtra("job",jobId)
            startActivity(intent)
        }

        binding.title.setOnClickListener { onBackPressed() }
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

                val response = ServiceApi.retrofitService.getJobProperty(jobId.toInt(),AppPreferences.getUserData(Params.UserId).toInt(),map)

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
//        val intent =  Intent(this,PropertyDetail::class.java)
//        startActivity(intent)

        val view: View = LayoutInflater.from(this).inflate(R.layout.single_bid_list_item,null);

        val dialog = MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
            .setView(view)
            .setBackground(ColorDrawable(Color.TRANSPARENT))
            .create()

        val acceptBtn = view.findViewById<Button>(R.id.accept)
        val rejectBtn = view.findViewById<Button>(R.id.reject)
//        val bidAmount = view.findViewById<EditText>(R.id.amount)
//        val title = view.findViewById<EditText>(R.id.title)

        acceptBtn.setOnClickListener {
            dialog.cancel()
            bidPopup(currentItem)
        }

        dialog.show()
    }

    private fun bidPopup(currentItem: JobPropertyList) {
        coroutineScope.launch {
            try {

                val map = HashMap<String,String>()
                map["Id"] = currentItem.jobPropertyBidId.toString()
                map["JobPropertyId"] = currentItem.id.toString()
                map["UserId"] = AppPreferences.getUserData(Params.UserId)

                val response = ServiceApi.retrofitService.jobPropertyBid(map)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("bid Popup", response.code().toString())
                        Log.d("bid Popup", response.body().toString())

                        toast("Bid Accepted Successfully")
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