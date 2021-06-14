package com.illopen.agent.ui.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.RangeSlider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.illopen.agent.R
import com.illopen.agent.adapters.CompletedJobDetailsAdapter
import com.illopen.agent.adapters.CompletedJobsAdapter
import com.illopen.agent.adapters.MyCompletedJobsAdapter
import com.illopen.agent.databinding.CompletedJobsFragmentBinding
import com.illopen.agent.model.AvailableJobs
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.agent.model.CompletedJobsAssignList
import com.illopen.agent.model.CompletedMyPostedJobsList
import com.illopen.agent.model.JobPropertyList
import com.illopen.agent.ui.activities.CompletedJobAssignDetails
import com.illopen.agent.ui.activities.CompletedJobDetails
import com.illopen.properybroker.utils.toast
import kotlinx.coroutines.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CompletedJobsFragment : Fragment(R.layout.completed_jobs_fragment),
    CompletedJobsAdapter.OnCompletedJobClick,
    MyCompletedJobsAdapter.OnCompletedAssignClickListener,
    CompletedJobsAdapter.OnMarkerClick, CompletedJobsAdapter.OnClickRating {

    private var _binding: CompletedJobsFragmentBinding? = null
    private val binding get() = _binding!!
    private var otherNewJobs = true

    private val TAG: String = "CompletedJobsFragment"

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    private var startDateSelect: String = ""
    private var endDateSelect: String = ""
    private var startTimeSelect: String = ""
    private var endTimeSelect: String = ""

    private lateinit var ratingPopUp: Dialog

    private lateinit var assignAdapter: MyCompletedJobsAdapter
    private lateinit var postAdapter: CompletedJobsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = CompletedJobsFragmentBinding.bind(view)

        getAssignedJob()

        binding.otherJobs.setOnClickListener {
            otherNewJobs = true
            binding.otherJobs.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_users_tabbar)
            binding.myJobs.setBackgroundColor(Color.TRANSPARENT)
            getAssignedJob()
        }

        binding.myJobs.setOnClickListener {
            otherNewJobs = false
            binding.otherJobs.setBackgroundColor(Color.TRANSPARENT)
            binding.myJobs.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_users_tabbar)
            getMyPostedCompletedJobs()
        }

        binding.filter.setOnClickListener {
            showFilterDialog()
        }
    }

    private fun getAssignedJob() {
        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getAssignedCompletedJobs(
                    3, false, AppPreferences.getUserData(Params.UserId).toInt(), map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("Completed Assign Job", response.code().toString())
                        Log.d("Completed Assign Job", response.body().toString())

                        val list: ArrayList<CompletedJobsAssignList> = response.body()!!.values!!

                        assignAdapter = MyCompletedJobsAdapter(
                            Params.MY_COMPLETED_JOBS,
                            list,
                            this@CompletedJobsFragment
                        )
                        binding.completedJobRv.adapter = assignAdapter

                        binding.searchBar.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {

                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {

                            }

                            override fun afterTextChanged(s: Editable?) {
                                if (s.toString().isNotEmpty()) {
                                    assignFilterList(s.toString(), list)
                                } else {
                                    assignAdapter.updateList(list)
                                }
                            }
                        })
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

    private fun assignFilterList(filterItem: String, list: List<CompletedJobsAssignList>) {

        val tempList: ArrayList<CompletedJobsAssignList> = ArrayList()

        for (i in list) {

            if (filterItem in i.jobNo.toString() || filterItem.toLowerCase() in i.userName!!.toLowerCase()) {

                tempList.add(i)
            }
        }
        assignAdapter.updateList(tempList)

    }


    private fun getMyPostedCompletedJobs() {
        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getCompletedMyPostedJobs(
                    AppPreferences.getUserData(Params.UserId).toInt(), 3, false, map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("getCompletedJobs", response.code().toString())
                        Log.d("getCompletedJobs", response.body().toString())

                        val list: ArrayList<CompletedMyPostedJobsList> = response.body()!!.values!!
                        postAdapter = CompletedJobsAdapter(
                            Params.MY_POSTED_COMPLETED_JOBS, list,
                            this@CompletedJobsFragment,
                            this@CompletedJobsFragment,
                            this@CompletedJobsFragment
                        )
                        binding.completedJobRv.adapter = postAdapter


                        binding.searchBar.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {

                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {

                            }

                            override fun afterTextChanged(s: Editable?) {
                                if (s.toString().isNotEmpty()) {
                                    postedFilterList(s.toString(), list)
                                } else {
                                    postAdapter.updateList(list)
                                }
                            }

                        })
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

    private fun postedFilterList(filterItem: String, list: List<CompletedMyPostedJobsList>) {

        val tempList: ArrayList<CompletedMyPostedJobsList> = ArrayList()

        for (i in list) {

            if (filterItem in i.jobNo.toString() || filterItem.toLowerCase() in i.userName!!.toLowerCase()) {

                tempList.add(i)
            }
        }
        postAdapter.updateList(tempList)
    }

    @SuppressLint("CutPasteId")
    private fun showFilterDialog() {
        val mDialog = Dialog(requireContext(), R.style.Theme_PropertyMainBroker)
        mDialog.setContentView(R.layout.filter_layout)
        mDialog.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        val startDate = mDialog.findViewById<TextView>(R.id.startDate)
        val endDate = mDialog.findViewById<TextView>(R.id.endDate)
        val startTime = mDialog.findViewById<TextView>(R.id.timeFrom)
        val endTime = mDialog.findViewById<TextView>(R.id.timeTo)
        val filter = mDialog.findViewById<Button>(R.id.searchFilter)

        startDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    var selectedMonthh = (monthOfYear + 1).toString()
                    if (selectedMonthh.toInt() < 10) {
                        selectedMonthh = "0$selectedMonthh"
                    }
                    startDateSelect = "$year/$selectedMonthh/$dayOfMonth"
                    startDate.run {
                        setText(startDateSelect)
                    }
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        endDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    var selectedMonthh = (monthOfYear + 1).toString()
                    if (selectedMonthh.toInt() < 10) {
                        selectedMonthh = "0$selectedMonthh"
                    }
                    endDateSelect = "$year/$selectedMonthh/$dayOfMonth"
                    endDate.run {
                        setText(endDateSelect)
                    }
                },
                year,
                month,
                day
            )
            dpd.show()
        }

//        rangeSlider.setLabelFormatter { value: Float ->
//            val format = NumberFormat.getCurrencyInstance()
//            format.maximumFractionDigits = 0
//            format.currency = Currency.getInstance("USD")
//            format.format(value.toDouble())
//        }

        startTime.setOnClickListener {
            val now = Calendar.getInstance()
            val hour = now.get(Calendar.HOUR_OF_DAY)
            val min = now.get(Calendar.MINUTE)

            val timePicker = TimePickerDialog(
                requireContext(),
                { view, hourOfDay, minute ->

                    val datetime = Calendar.getInstance()
                    datetime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    datetime.set(Calendar.MINUTE, minute)

                    val timeFormatAmPm = SimpleDateFormat("hh:mm aa", Locale.US)
                    val timeFormat24Hours = SimpleDateFormat("HH:mm")

                    val dateFormat: String = timeFormatAmPm.format(datetime.time)
                    startTimeSelect = dateFormat

                    startTime.text = when (view.is24HourView) {
                        true -> timeFormat24Hours.format(datetime.time)
                        false -> timeFormatAmPm.format(datetime.time)
                    }

                },
                hour,
                min,
                false
            )
            timePicker.show()

        }

        endTime.setOnClickListener {
            val now = Calendar.getInstance()
            val hour = now.get(Calendar.HOUR_OF_DAY)
            val min = now.get(Calendar.MINUTE)

            val timePicker = TimePickerDialog(
                requireContext(),
                { view, hourOfDay, minute ->

                    val datetime = Calendar.getInstance()
                    datetime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    datetime.set(Calendar.MINUTE, minute)

                    val timeFormatAmPm = SimpleDateFormat("hh:mm aa", Locale.US)
                    val timeFormat24Hours = SimpleDateFormat("HH:mm")

                    val dateFormat: String = timeFormatAmPm.format(datetime.time)
                    endDateSelect = dateFormat

                    endTime.text = when (view.is24HourView) {
                        true -> timeFormat24Hours.format(datetime.time)
                        false -> timeFormatAmPm.format(datetime.time)
                    }

                },
                hour,
                min,
                false
            )
            timePicker.show()
        }

        filter.setOnClickListener {

            if (startDateSelect.isEmpty()) {
                requireActivity().toast("please select start date")
            } else if (endDateSelect.isEmpty()) {
                requireActivity().toast("please select end date")
            } else {

                searchAssignDataAPI()
                searchPostedDataAPI()
                mDialog.dismiss()
            }
        }
        mDialog.show()
    }

    private fun searchAssignDataAPI() {
        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.searchCompleteAssignJob(
                    3,
                    false, startDateSelect,
                    endDateSelect, startTimeSelect, endTimeSelect, map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("searchData", response.code().toString())
                        Log.d("searchData", response.body().toString())

                        val list: ArrayList<CompletedJobsAssignList> = response.body()!!.values!!
                        binding.completedJobRv.adapter = MyCompletedJobsAdapter(
                            Params.MY_COMPLETED_JOBS,
                            list,
                            this@CompletedJobsFragment
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

    private fun searchPostedDataAPI() {
        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.searchCompleteJob(
                    AppPreferences.getUserData(Params.UserId).toInt(),3,
                    false, startDateSelect,
                    endDateSelect, startTimeSelect, endTimeSelect, map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("searchData", response.code().toString())
                        Log.d("searchData", response.body().toString())

                        val list: ArrayList<CompletedMyPostedJobsList> = response.body()!!.values!!
                        binding.completedJobRv.adapter = CompletedJobsAdapter(
                            Params.MY_POSTED_COMPLETED_JOBS, list,
                            this@CompletedJobsFragment,
                            this@CompletedJobsFragment,
                            this@CompletedJobsFragment
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCompletedJobsClick(currentItem: CompletedMyPostedJobsList) {

        val intent = Intent(activity, CompletedJobDetails::class.java)
        intent.putExtra("CompletedMyPostedJob", currentItem.id.toString())
        intent.putExtra("AssignedUserId", currentItem.assignedUserId.toString())
        intent.putExtra(Params.SUB_FROM, Params.MY_POSTED_COMPLETED_JOBS)
        startActivity(intent)
    }

    override fun onItemClick(itemPosition: Int, data: CompletedJobsAssignList) {
        val intent = Intent(activity, CompletedJobAssignDetails::class.java)
        intent.putExtra("CompletedAssignJob", data.id.toString())
        intent.putExtra(Params.SUB_FROM, Params.MY_COMPLETED_JOBS)
        startActivity(intent)
    }

    override fun onCompletedMarkerClick(currentItem: CompletedMyPostedJobsList) {

        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("Are You Sure ?")
        builder.setMessage("Are You Sure Mark As Completed!")
        builder.setPositiveButton("YES, do it!") { dialogInterface: DialogInterface, i: Int ->
            completedMarked(currentItem)
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
        }
        builder.show()
    }

    private fun completedMarked(currentItem: CompletedMyPostedJobsList) {
        coroutineScope.launch {
            try {
                val response = ServiceApi.retrofitService.markJobPropertyStatus(
                    currentItem.id!!.toInt(),
                    4
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("Mark Completed Property", response.code().toString())
                        Log.d("Mark CompletedProperty", response.body().toString())

                        if (response.code() == 200) {
                            getMyPostedCompletedJobs()
                            requireActivity().toast("Completed Job Successfully")
                        } else {

                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "something wrong ")
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
            }
        }
    }

    override fun onClickRating(position: Int, currentItem: CompletedMyPostedJobsList) {
        ratingPopUp = Dialog(requireContext(), R.style.Theme_PropertyMainBroker)
        ratingPopUp.setContentView(R.layout.agent_job_rating_popup)
        ratingPopUp.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        val rating = ratingPopUp.findViewById<RatingBar>(R.id.ratingView)
        val review = ratingPopUp.findViewById<TextInputEditText>(R.id.review)
        val reviewBtn = ratingPopUp.findViewById<Button>(R.id.reviewBtn)

        if (currentItem.jobRatting!! > 0) {
            rating.rating = currentItem.jobRatting.toFloat()
            review.setText(currentItem.jobReview.toString().trim())
            reviewBtn.text = "Update Review"
        }

        reviewBtn.setOnClickListener {
            val ratings = rating.rating.toInt().toString()
            val reviews = review.text.toString().trim()

            if (reviews.isNotEmpty()) {
                ratingAPI(ratings, reviews, currentItem)
                ratingPopUp.dismiss()
            } else {
                requireActivity().toast("Please enter review")
            }
        }
        ratingPopUp.show()
    }

    private fun ratingAPI(
        ratings: String,
        reviews: String,
        currentItem: CompletedMyPostedJobsList
    ) {
        coroutineScope.launch {
            try {
                val data = HashMap<String, String>()
                data["JobId"] = currentItem.id.toString()
                data["RattingById"] = AppPreferences.getUserData(Params.UserId)
                data["RattingToId"] = currentItem.assignedUserId.toString()
                data["Ratting"] = ratings.toInt().toString()
                data["Review"] = reviews

                val response = ServiceApi.retrofitService.userAgentRating(data)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        if (response.code() == 200) {
                            getMyPostedCompletedJobs()
                            requireContext().toast("Review Submit Successfully")
                        } else {
                            requireContext().toast("something wrong")
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