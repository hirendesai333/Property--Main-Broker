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
import android.widget.EditText
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.soboft.propertybroker.R
import com.soboft.propertybroker.adapters.JobPropertyAdapter
import com.soboft.propertybroker.databinding.ActivityNewJobDetailsBinding
import com.soboft.propertybroker.databinding.ActivityProfileBinding
import com.soboft.propertybroker.listeners.OnJobPropertyClick
import com.soboft.propertybroker.model.JobPropertyList
import com.soboft.propertybroker.network.ServiceApi
import com.soboft.propertybroker.utils.AppPreferences
import com.soboft.propertybroker.utils.Params
import com.soboft.properybroker.utils.toast
import kotlinx.coroutines.*
import java.util.HashMap


class NewJobDetails : AppCompatActivity(), OnJobPropertyClick {

    private lateinit var binding: ActivityNewJobDetailsBinding

    private val TAG = "NewJobDetails"

    private lateinit var jobId: String
//    private lateinit var from : String

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewJobDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        jobId = intent.getStringExtra("JobData")!!

//        from = intent.getStringExtra(Params.FROM)!!

        getJobProperty()

        binding.addAllBid.setOnClickListener {

            val intent =  Intent(this,AllJobPropertyBidList::class.java)
            intent.putExtra("job",jobId)
            startActivity(intent)
        }
        binding.title.setOnClickListener {
            onBackPressed()
        }
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

                val response = ServiceApi.retrofitService.getJobProperty(jobId.toInt(),
                    AppPreferences.getUserData(Params.UserId).toInt(),
                    map)

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

          showBidPopup(currentItem)
    }

    private fun showBidPopup(currentItem: JobPropertyList) {
        val view: View = LayoutInflater.from(this).inflate(R.layout.bid_layout_popup,null);

        val dialog = MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
            .setView(view)
            .setBackground(ColorDrawable(Color.TRANSPARENT))
            .create()

        val bidButton = view.findViewById<Button>(R.id.bidSubmit)
        val bidAmount = view.findViewById<EditText>(R.id.bidAmount)
        val description = view.findViewById<EditText>(R.id.description)

//        if (currentItem!!.bidAmount?.toInt() == 1) {
//            bidAmount.setText("1500")
//        } else {
//            bidAmount.setText("")
//        }

        bidButton.setOnClickListener {
//            binding.bidAmountLayout.visibility = View.VISIBLE
//            binding.bid.text = "Edit Bid"
//            binding.bidOriginalAmount.text = "Bid amount: $${bidAmount.text.toString().trim()}"
            val amount = bidAmount.text.toString().trim()
            val desc = description.text.toString().trim()

            bidingData(amount,desc,currentItem)
            dialog.cancel()
        }

        dialog.show()
    }

    private fun bidingData(bidAmount : String,description : String,currentItem: JobPropertyList) {
        coroutineScope.launch {
            try {

                val map = HashMap<String,String>()
                map["Id"] = currentItem.jobPropertyBidId.toString()
                map["JobPropertyId"] = currentItem.id.toString()
                map["UserId"] = AppPreferences.getUserData(Params.UserId)
                map["Amount"] = bidAmount
                map["Note"] = description

                val response = ServiceApi.retrofitService.jobPropertyBid(map)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("bidingData", response.code().toString())
                        Log.d("bidingData", response.body().toString())

                        toast("Bid Added Successfully")
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