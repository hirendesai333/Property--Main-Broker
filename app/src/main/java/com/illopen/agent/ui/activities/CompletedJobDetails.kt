package com.illopen.agent.ui.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RatingBar
import com.google.android.material.textfield.TextInputEditText
import com.illopen.agent.R
import com.illopen.agent.adapters.CompletedJobDetailsAdapter
import com.illopen.agent.databinding.ActivityCompletedJobDetailsBinding
import com.illopen.agent.model.JobPropertyList
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.properybroker.utils.toast
import kotlinx.coroutines.*
import java.util.HashMap

class CompletedJobDetails : AppCompatActivity(), CompletedJobDetailsAdapter.OnCompletedClick {

    private lateinit var binding: ActivityCompletedJobDetailsBinding

    private val TAG = "CompletedJobDetails"

    private lateinit var jobId: String

    private lateinit var assignJobId: String

    private lateinit var ratingPopUp: Dialog

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompletedJobDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        jobId = intent.getStringExtra("CompletedMyPostedJob")!!

        assignJobId = intent.getStringExtra("AssignedUserId")!!

        binding.allBidPropertyDetails.setOnClickListener {
            val intent = Intent(this, CompletedJobBidList::class.java)
            intent.putExtra("job", jobId)
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

                val response = ServiceApi.retrofitService.getJobProperty(
                    jobId.toInt(),
                    AppPreferences.getUserData(Params.UserId).toInt(), map
                )

                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("getCompletedJob", response.code().toString())
                        Log.d("getCompletedJob", response.body().toString())

                        val list: List<JobPropertyList> = response.body()!!.values!!

                        binding.completedJobProperty.adapter = CompletedJobDetailsAdapter(
                            this@CompletedJobDetails, list,
                            this@CompletedJobDetails
                        )

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

    override fun onCompletedClick(position: Int, currentItem: JobPropertyList) {
        ratingPopUp = Dialog(this, R.style.Theme_PropertyMainBroker)
        ratingPopUp.setContentView(R.layout.agent_job_rating_popup)
        ratingPopUp.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        val rating = ratingPopUp.findViewById<RatingBar>(R.id.ratingView)
        val review = ratingPopUp.findViewById<TextInputEditText>(R.id.review)
        val reviewBtn = ratingPopUp.findViewById<Button>(R.id.reviewBtn)

        if (currentItem.rating!! > 0) {
            rating.rating = currentItem.rating.toFloat()
            review.setText(currentItem.review.toString().trim())
            reviewBtn.text = "Update Review"
        }

        reviewBtn.setOnClickListener {
            val ratings = rating.rating.toInt().toString()
            val reviews = review.text.toString().trim()

            if (reviews.isNotEmpty()) {
                ratingAPI(ratings, reviews, currentItem)
                ratingPopUp.dismiss()
            } else {
                toast("Please enter review")
            }
        }
        ratingPopUp.show()
    }

    private fun ratingAPI(ratings: String, reviews: String, currentItem: JobPropertyList) {
        coroutineScope.launch {
            try {
                val data = HashMap<String, String>()
                data["JobId"] = jobId
                data["RattingById"] = AppPreferences.getUserData(Params.UserId)
                data["RattingToId"] = assignJobId
                data["Ratting"] = ratings
                data["Review"] = reviews

                val response = ServiceApi.retrofitService.userAgentRating(data)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        if (response.code() == 200) {
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