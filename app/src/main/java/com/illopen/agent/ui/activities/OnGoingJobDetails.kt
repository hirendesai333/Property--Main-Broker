package com.illopen.agent.ui.activities

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RatingBar
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
import com.illopen.agent.utils.ProgressDialog
import com.illopen.properybroker.utils.toast
import kotlinx.coroutines.*
import java.util.HashMap

class OnGoingJobDetails : AppCompatActivity(), OnGoingJobAdapter.JobPropertyReviewClick {

    private lateinit var binding: ActivityOnGoingJobDetailsBinding

    private val TAG = "OnGoingJobDetails"

    private lateinit var jobId: String

    private lateinit var progressDialog: ProgressDialog

    private lateinit var reviewPopup: Dialog

    private var isPropertyShown: Boolean = false

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnGoingJobDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        jobId = intent.getStringExtra("OnGoingData")!!
        progressDialog = ProgressDialog(this)
        getOnGoingJobProperty()

//        binding.allJobBid.setOnClickListener {
//            val intent =  Intent(this,AllJobPropertyBidList::class.java)
//            intent.putExtra("job",jobId)
//            startActivity(intent)
//        }

        binding.mark.setOnClickListener {
            if (isPropertyShown) {
                markAsShownProperty()
            } else {
                toast("First Review All The Properties")
            }
        }

        binding.title.setOnClickListener { onBackPressed() }
    }

    private fun getOnGoingJobProperty() {
        progressDialog.dialog.show()
        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getJobProperty(
                    jobId.toInt(),
                    AppPreferences.getUserData(Params.UserId).toInt(),
                    map
                )

                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("getOnGoingJobProperty", response.code().toString())
                        Log.d("getOnGoingJobProperty", response.body().toString())

                        val list: List<JobPropertyList> = response.body()!!.values!!
                        binding.onGoingJobProperty.adapter = OnGoingJobAdapter(
                            this@OnGoingJobDetails, list,
                            this@OnGoingJobDetails
                        )

                        for (i in list.indices) {

                            if (list[i].rating == 0) {
                                isPropertyShown = false
                                break
                            } else {
                                isPropertyShown = true
                            }
                        }
                        progressDialog.dialog.dismiss()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "something wrong")
                        progressDialog.dialog.dismiss()
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                progressDialog.dialog.dismiss()
            }
        }

    }

    private fun markAsShownProperty() {

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


    private fun marked() {
        progressDialog.dialog.show()
        coroutineScope.launch {
            try {
                val response = ServiceApi.retrofitService.markJobPropertyStatus(
                    jobId.toInt(),
                    3
                )

                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("MarkProperty", response.code().toString())
                        Log.d("MarkProperty", response.body().toString())

                        if (response.code() == 200) {
                            toast("Marked Property Successfully")
//                            startActivity(Intent(this@OnGoingJobDetails, UpcomingJobsFragment::class.java))
//                            finish()
                        } else {
                            toast("something wrong")
                        }
                        progressDialog.dialog.dismiss()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "something wrong ")
                        progressDialog.dialog.dismiss()
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                progressDialog.dialog.dismiss()
            }
        }
    }

    override fun onGoingJobReviewClick(position: Int, currentItem: JobPropertyList) {

        reviewPopup = Dialog(this, R.style.Theme_PropertyMainBroker)
        reviewPopup.setContentView(R.layout.job_property_review_popup)
        reviewPopup.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        val rating = reviewPopup.findViewById<RatingBar>(R.id.rating)
        val review = reviewPopup.findViewById<TextInputEditText>(R.id.edtReview)
        val note = reviewPopup.findViewById<TextInputEditText>(R.id.note)
        val btnSave = reviewPopup.findViewById<Button>(R.id.btnReview)

        if (currentItem.rating!! > 0) {
            rating.rating = currentItem.rating.toFloat()
            review.setText(currentItem.review.toString())
            note.setText(currentItem.note.toString())
            btnSave.text = "Update Review"
        }

        btnSave.setOnClickListener {

            val ratings = rating.rating.toInt().toString()
            val reviews = review.text.toString().trim()
            val notes = note.text.toString().trim()

            if (reviews.isNotEmpty() && notes.isNotEmpty()) {
                addReviewPopup(ratings, reviews, notes, currentItem)
                reviewPopup.dismiss()
            } else {
                toast("Please enter review")
            }
        }
        reviewPopup.show()
    }

    private fun addReviewPopup(
        rating: String,
        reviews: String,
        notes: String,
        currentItem: JobPropertyList
    ) {

        coroutineScope.launch {
            try {
                val data = HashMap<String, String>()
                data["Id"] = currentItem.id.toString()
                data["Rating"] = rating
                data["Review"] = reviews
                data["Note"] = notes
                data["RatingBy"] = "5"
                val response = ServiceApi.retrofitService.jobPropertyUpdateShown(data)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        if (response.code() == 200) {
                            getOnGoingJobProperty()
                            toast("Review Submit Successfully")
                        } else {
                            toast("something wrong")
                        }
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