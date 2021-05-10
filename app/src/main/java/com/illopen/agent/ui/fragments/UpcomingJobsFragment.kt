package com.illopen.agent.ui.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.android.material.timepicker.MaterialTimePicker
import com.illopen.agent.R
import com.illopen.agent.adapters.UpComingMyPostedJobAdapter
import com.illopen.agent.adapters.UpcomingBidsAdapter
import com.illopen.agent.databinding.FragmentUpcomingJobsBinding
import com.illopen.agent.model.AssignedJobList
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.Params
import com.illopen.agent.listeners.OnGoingClick
import com.illopen.agent.model.OngoingMyPostedJobList
import com.illopen.agent.model.OngoingMyPostedJobModel
import com.illopen.agent.ui.activities.OnGoingJobDetails
import com.illopen.agent.ui.activities.OnGoingMyPostedJobDetails
import com.illopen.agent.utils.AppPreferences
import com.illopen.properybroker.utils.toast
import kotlinx.android.synthetic.main.add_customer_popup.view.*
import kotlinx.coroutines.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UpcomingJobsFragment : Fragment(R.layout.fragment_upcoming_jobs), OnGoingClick,
    UpComingMyPostedJobAdapter.OnMyPostedJobClick {
    private val TAG: String = "UpcomingJobsFragment"
    private var _binding: FragmentUpcomingJobsBinding? = null
    private val binding get() = _binding!!
    private var otherNewJobs = true

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    private var startDateSelect: String = ""
    private var endDateSelect: String = ""
    private var startTimeSelect: String = ""
    private var endTimeSelect: String = ""

    private lateinit var assignAdapter: UpcomingBidsAdapter
    private lateinit var postedJobAdapter: UpComingMyPostedJobAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUpcomingJobsBinding.bind(view)

        getAssignedJobs()

        binding.filter.setOnClickListener {
            showFilterDialog()
        }

        binding.assignJobs.setOnClickListener {
            otherNewJobs = true
            binding.assignJobs.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_users_tabbar)
            binding.postedJobs.setBackgroundColor(Color.TRANSPARENT)
            getAssignedJobs()
        }

        binding.postedJobs.setOnClickListener {
            otherNewJobs = false
            binding.assignJobs.setBackgroundColor(Color.TRANSPARENT)
            binding.postedJobs.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_users_tabbar)
            getMyPostedJob()
        }

    }

    private fun getMyPostedJob() {
        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.onGoingMyPostedJobs(
                    AppPreferences.getUserData(Params.UserId).toInt(), 2, false, map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("getOngoingMyPostedJob", response.code().toString())
                        Log.d("getOngoingMyPostedJob", response.body().toString())

                        val list: ArrayList<OngoingMyPostedJobList> = response.body()!!.values!!

                        postedJobAdapter = UpComingMyPostedJobAdapter(
                            Params.MY_POSTED_ONGOING_JOBS, list,
                            this@UpcomingJobsFragment
                        )

                        binding.upcomingJobsRv.adapter = postedJobAdapter

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
                                    postedJobAdapter.updateList(list)
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
                Log.d("getOngoingMyPostedJob", e.message.toString())
            }
        }

    }

    private fun postedFilterList(filterItem: String, list: List<OngoingMyPostedJobList>) {
        val tempList: ArrayList<OngoingMyPostedJobList> = ArrayList()

        for (i in list) {

            if (filterItem in i.jobNo.toString() || filterItem.toLowerCase() in i.userName!!.toLowerCase()) {

                tempList.add(i)
            }
        }
        postedJobAdapter.updateList(tempList)
    }

    private fun assignFilterList(filterItem: String, list: List<AssignedJobList>) {

        val tempList: ArrayList<AssignedJobList> = ArrayList()

        for (i in list) {

            if (filterItem in i.jobNo.toString() || filterItem.toLowerCase() in i.userName!!.toLowerCase()) {

                tempList.add(i)
            }
        }
        assignAdapter.updateList(tempList)

    }

    private fun getAssignedJobs() {
        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getOnGoingJobsAssigned(
                    2, false, AppPreferences.getUserData(Params.UserId).toInt(), map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("getAssignedJob", response.code().toString())
                        Log.d("getAssignedJob", response.body().toString())

                        val list: ArrayList<AssignedJobList> = response.body()!!.values!!
                        assignAdapter = UpcomingBidsAdapter(
                            Params.JOB_ASSIGN_TO_ME,
                            list,
                            this@UpcomingJobsFragment
                        )
                        binding.upcomingJobsRv.adapter = assignAdapter

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
                Log.d("getAssignedJob", e.message.toString())
            }
        }
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

    private fun searchPostedDataAPI() {
        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.searchPostedOngoingJob(AppPreferences.getUserData(Params.UserId).toInt(),
                    2,false, startDateSelect, endDateSelect, startTimeSelect, endTimeSelect, map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("searchData", response.code().toString())
                        Log.d("searchData", response.body().toString())

                        if (response.code() == 200) {
                            requireContext().toast("Search Data Success..")
                        } else {
                            requireContext().toast("something wrong")
                        }

                        val list: List<OngoingMyPostedJobList> = response.body()!!.values!!
                        binding.upcomingJobsRv.adapter = UpComingMyPostedJobAdapter(
                            Params.MY_POSTED_ONGOING_JOBS, list, this@UpcomingJobsFragment)

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

    private fun searchAssignDataAPI() {

        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.searchOngoingJob(
                    2,false, startDateSelect, endDateSelect, startTimeSelect, endTimeSelect, map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("searchData", response.code().toString())
                        Log.d("searchData", response.body().toString())

                        if (response.code() == 200) {
                            requireContext().toast("Search Data Success..")
                        } else {
                            requireContext().toast("something wrong")
                        }

                        val list: ArrayList<AssignedJobList> = response.body()!!.values!!
                        binding.upcomingJobsRv.adapter = UpcomingBidsAdapter(
                            Params.JOB_ASSIGN_TO_ME, list, this@UpcomingJobsFragment)

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

    override fun onGoingClick(currentItem: AssignedJobList) {
//        Intent(activity, OnGoingJobDetails::class.java).apply {
//            putExtra("OnGoingData", currentItem.id.toString())
//            putExtra(Params.FROM, Params.ONGOING_JOBS_FRAGMENT)
//            if (otherNewJobs) {
//                putExtra(Params.SUB_FROM, Params.JOB_ASSIGN_TO_ME)
//            } else {
//                putExtra(Params.SUB_FROM, Params.MY_POSTED_ONGOING_JOBS)
//            }
//            startActivity(this)
//        }

        val intent = Intent(activity, OnGoingJobDetails::class.java)
        intent.putExtra("OnGoingData", currentItem.id.toString())
        intent.putExtra(Params.SUB_FROM, Params.JOB_ASSIGN_TO_ME)
        startActivity(intent)
    }

    override fun onMyPostedClick(position: Int, currentItem: OngoingMyPostedJobList) {

        val intent = Intent(activity, OnGoingMyPostedJobDetails::class.java)
        intent.putExtra("OnGoingMyPostedData", currentItem.id.toString())
        intent.putExtra(Params.SUB_FROM, Params.MY_POSTED_ONGOING_JOBS)
        startActivity(intent)
    }
}