package com.soboft.propertybroker.ui.activities

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.soboft.propertybroker.R
import com.soboft.propertybroker.adapters.OnGoingJobAdapter
import com.soboft.propertybroker.databinding.ActivityOnGoingJobDetailsBinding
import com.soboft.propertybroker.model.JobPropertyList
import com.soboft.propertybroker.network.ServiceApi
import com.soboft.propertybroker.utils.AppPreferences
import com.soboft.propertybroker.utils.Params
import com.soboft.properybroker.utils.toast
import kotlinx.coroutines.*
import java.util.HashMap

class OnGoingJobDetails : AppCompatActivity(), OnGoingJobAdapter.JobPropertyClick {

    private lateinit var binding: ActivityOnGoingJobDetailsBinding

    private val TAG = "OnGoingJobDetails"

    private lateinit var jobId: String

    private lateinit var reviewPopup : Dialog

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

    override fun onGoingJobPropertyClick(position : Int, currentItem: JobPropertyList) {

        reviewPopup = Dialog(this, R.style.Theme_PropertyMainBroker)
        reviewPopup.setContentView(R.layout.job_property_review_popup)
        reviewPopup.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        reviewPopup.show()

        reviewPopup.findViewById<Button>(R.id.btnReview).setOnClickListener {

            val rating = reviewPopup.findViewById<RatingBar>(R.id.rating)
            val review = reviewPopup.findViewById<TextInputEditText>(R.id.edtReview).text.toString().trim()
            val note = reviewPopup.findViewById<TextInputEditText>(R.id.note).text.toString().trim()

            Toast.makeText(this, "Submit Review Successful..", Toast.LENGTH_SHORT).show()
            addReviewPopup(rating,review,note,currentItem)
            reviewPopup.dismiss()
        }

        markProperty()
    }

    private fun markProperty() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are You Sure Mark This Property!!")
        builder.setPositiveButton("YES") { dialogInterface: DialogInterface, i: Int ->
            marked()
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("NO") { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
        }
        builder.show()
    }

    private fun marked() {
        coroutineScope.launch {
            try {
                val response = ServiceApi.retrofitService.markJobPropertyStatus(
                   jobId.toInt(),
                   3
                )

                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("MarkProperty", response.code().toString())
                        Log.d("MarkProperty", response.body().toString())

                        toast("Marked Property Successfully")
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

    private fun addReviewPopup(rating: RatingBar, review: String, note: String,currentItem: JobPropertyList) {

        coroutineScope.launch {
            try {
                val data = HashMap<String,String>()
                data["Id"] = currentItem.id.toString()
                data["Rating"] = rating.rating.toString()
                data["Review"] = review
                data["Note"] = note
                data["RatingBy"] = "5"
                val response = ServiceApi.retrofitService.jobPropertyUpdateShown(data)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("Review Details", response.code().toString())
                        Log.d("Review Details", response.body().toString())

                        toast("Review Submit Successfully")
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