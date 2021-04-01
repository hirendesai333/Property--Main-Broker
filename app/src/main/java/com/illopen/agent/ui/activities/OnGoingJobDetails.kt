package com.illopen.agent.ui.activities

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.illopen.agent.R
import com.illopen.agent.adapters.OnGoingJobAdapter
import com.illopen.agent.databinding.ActivityOnGoingJobDetailsBinding
import com.illopen.agent.model.JobPropertyList
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.ui.fragments.UpcomingJobsFragment
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.properybroker.utils.toast
import kotlinx.coroutines.*
import java.util.HashMap

class OnGoingJobDetails : AppCompatActivity(), OnGoingJobAdapter.JobPropertyMarkClick,OnGoingJobAdapter.JobPropertyReviewClick {

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

//        binding.allJobBid.setOnClickListener {
//            val intent =  Intent(this,AllJobPropertyBidList::class.java)
//            intent.putExtra("job",jobId)
//            startActivity(intent)
//        }

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

                        if (list.isNotEmpty()){
                            binding.onGoingJobProperty.adapter = OnGoingJobAdapter(this@OnGoingJobDetails,list,
                                this@OnGoingJobDetails,this@OnGoingJobDetails)
                        }else{
                            // no property found
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

    override fun onGoingJobMarkClick(position: Int, currentItem: JobPropertyList) {

        if (currentItem.review.isNullOrEmpty()){
                toast("Please Review the Property To Change The Job Status!!")
            }else{

            val builder = MaterialAlertDialogBuilder(this)
            builder.setTitle("Are You Sure ?")
            builder.setMessage("Are You Sure Mark This Property!!")
            builder.setPositiveButton("Yes, do it!") { dialogInterface: DialogInterface, i: Int ->

            marked()
            dialogInterface.dismiss()
            }

            builder.setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
            builder.show()
        }
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

                        if (response.code() == 200){
                            toast("Marked Property Successfully")
                        }else{
                            //not marked
                        }

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

    override fun onGoingJobReviewClick(position: Int, currentItem: JobPropertyList) {

        reviewPopup = Dialog(this, R.style.Theme_PropertyMainBroker)
        reviewPopup.setContentView(R.layout.job_property_review_popup)
        reviewPopup.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        reviewPopup.findViewById<Button>(R.id.btnReview).setOnClickListener {

            val rating = reviewPopup.findViewById<RatingBar>(R.id.rating)
            val review = reviewPopup.findViewById<TextInputEditText>(R.id.edtReview).text.toString().trim()
            val note = reviewPopup.findViewById<TextInputEditText>(R.id.note).text.toString().trim()

            if (review.isNotEmpty()){
                addReviewPopup(rating,review,note,currentItem)
                reviewPopup.dismiss()
            }else{
                toast("Please enter review")
            }
        }
        reviewPopup.show()
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