package com.illopen.agent.ui.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.slider.RangeSlider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.gson.Gson
import com.illopen.agent.R
import com.illopen.agent.adapters.AllPropertyListAdapter
import com.illopen.agent.adapters.UpComingMyPostedJobAdapter
import com.illopen.agent.adapters.UpcomingBidsAdapter
import com.illopen.agent.databinding.FragmentUpcomingJobsBinding
import com.illopen.agent.model.AssignedJobList
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.Params
import com.illopen.agent.listeners.OnGoingClick
import com.illopen.agent.model.AvailableJobs
import com.illopen.agent.model.OngoingMyPostedJobList
import com.illopen.agent.ui.activities.OnGoingJobDetails
import com.illopen.agent.ui.activities.OnGoingMyPostedJobDetails
import com.illopen.agent.utils.AppPreferences
import kotlinx.coroutines.*
import java.text.NumberFormat
import java.util.*

class UpcomingJobsFragment : Fragment(R.layout.fragment_upcoming_jobs), OnGoingClick , UpComingMyPostedJobAdapter.OnMyPostedJobClick {
    private val TAG : String = "UpcomingJobsFragment"
    private var _binding: FragmentUpcomingJobsBinding? = null
    private val binding get() = _binding!!
    private var otherNewJobs = true

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    lateinit var timePicker: TimePickerDialog
    lateinit var datePicker: DatePickerDialog

    private lateinit var selectedDate: String
    private lateinit var selectedTime: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUpcomingJobsBinding.bind(view)

        getAssignedJobs()

        binding.search.setOnClickListener {
            showFilterDialog()
        }

        binding.assignJobs.setOnClickListener {
            otherNewJobs = true
            binding.assignJobs.background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_users_tabbar)
            binding.postedJobs.setBackgroundColor(Color.TRANSPARENT)
            getAssignedJobs()
        }

        binding.postedJobs.setOnClickListener {
            otherNewJobs = false
            binding.assignJobs.setBackgroundColor(Color.TRANSPARENT)
            binding.postedJobs.background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_users_tabbar)
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

                val response = ServiceApi.retrofitService.getOnGoingJobs(
                    AppPreferences.getUserData(Params.UserId).toInt(),2,false,map
                )
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getOngoingMyPostedJob", response.code().toString())
                        Log.d("getOngoingMyPostedJob",response.body().toString())

                        val list : List<OngoingMyPostedJobList> = response.body()!!.values!!

                        binding.upcomingJobsRv.adapter = UpComingMyPostedJobAdapter(Params.MY_POSTED_ONGOING_JOBS, list,this@UpcomingJobsFragment)

                    }
                }else{
                    withContext(Dispatchers.Main){
                        Log.d(TAG, "something wrong")
                    }
                }
            }catch (e : Exception){
                Log.d("getOngoingMyPostedJob", e.message.toString())
            }
        }

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
                    2,false,2,map
                )
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getAssignedJob", response.code().toString())
                        Log.d("getAssignedJob",response.body().toString())

                        val list : List<AssignedJobList> = response.body()!!.values!!

                        binding.upcomingJobsRv.adapter = UpcomingBidsAdapter(Params.JOB_ASSIGN_TO_ME, list, this@UpcomingJobsFragment)

                    }
                }else{
                    withContext(Dispatchers.Main){
                        Log.d(TAG, "something wrong")
                    }
                }
            }catch (e : Exception){
                Log.d("getOngoingAssignedJob", e.message.toString())
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
            val builder = MaterialDatePicker.Builder.datePicker()
            val picker = builder.build()
            val now = Calendar.getInstance()
            builder.setSelection(now.timeInMillis)
            picker.show(requireActivity().supportFragmentManager, picker.toString())
        }

        endDate.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val picker = builder.build()
            val now = Calendar.getInstance()
            builder.setSelection(now.timeInMillis)
            picker.show(requireActivity().supportFragmentManager, picker.toString())
        }

//        rangeSlider.setLabelFormatter { value: Float ->
//            val format = NumberFormat.getCurrencyInstance()
//            format.maximumFractionDigits = 0
//            format.currency = Currency.getInstance("USD")
//            format.format(value.toDouble())
//        }

        startTime.setOnClickListener {
            val buider = MaterialTimePicker.Builder()

            val now = Calendar.getInstance()
            val hour = now.get(Calendar.HOUR_OF_DAY)
            val min = now.get(Calendar.MINUTE)

            timePicker = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    selectedTime =
                        String.format("%d:%d", hourOfDay, minute)
                    startTime.text = selectedTime
                },
                hour,
                min,
                false
            )
            timePicker.show()
        }

        endTime.setOnClickListener {
            val builder = MaterialTimePicker.Builder()

            val now = Calendar.getInstance()
            val hour = now.get(Calendar.HOUR_OF_DAY)
            val min = now.get(Calendar.MINUTE)

            timePicker = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    selectedTime =
                        String.format("%d:%d", hourOfDay, minute)
                    endTime.text = selectedTime
                },
                hour,
                min,
                false
            )
            timePicker.show()
        }

        filter.setOnClickListener {
            val jobNo = mDialog.findViewById<TextView>(R.id.search).text.toString().trim()

            if (jobNo.isEmpty()){
                mDialog.findViewById<TextInputEditText>(R.id.search).error = "Field Can't be Empty"
                mDialog.findViewById<TextInputEditText>(R.id.search).requestFocus()
            }else{
                Toast.makeText(context, "Search Data Success..", Toast.LENGTH_SHORT).show()
                searchDataAPI(jobNo)
                mDialog.dismiss()
            }
        }
        mDialog.show()
    }

    private fun searchDataAPI(jobNo: String) {

        coroutineScope.launch {
            try {
                val data = HashMap<String,String>()
                data["JobNo"] = jobNo
//                data["VisitDateFrom"] = ""
//                data["VisitDateTo"] = ""
//                data["VisitTimeFrom"] = ""
//                data["VisitTimeTo"] = ""
                val response = ServiceApi.retrofitService.searchJobNo(false,jobNo,data)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("searchData", response.code().toString())
                        Log.d("searchData", response.body().toString())

//                        val list: List<AssignedJobList> = response.body()!!.values!!
//                        Log.d(TAG, "searchDataAPI: ${Gson().toJson(list)}")
//                        binding.upcomingJobsRv.adapter = AllPropertyListAdapter(
//                            Params.OTHER_NEW_JOBS, list,this@UpcomingJobsFragment)
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
        intent.putExtra("OnGoingData",currentItem.id.toString())
        intent.putExtra(Params.SUB_FROM,Params.JOB_ASSIGN_TO_ME)
        startActivity(intent)
    }

    override fun onMyPostedClick(position: Int, currentItem: OngoingMyPostedJobList) {

        val intent = Intent(activity, OnGoingMyPostedJobDetails::class.java)
        intent.putExtra("OnGoingMyPostedData",currentItem.id.toString())
        intent.putExtra(Params.SUB_FROM,Params.MY_POSTED_ONGOING_JOBS)
        startActivity(intent)
    }
}