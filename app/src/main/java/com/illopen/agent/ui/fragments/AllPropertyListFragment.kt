package com.illopen.agent.ui.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.gson.Gson
import com.illopen.agent.R
import com.illopen.agent.adapters.AllMyPostedJobsAdapter
import com.illopen.agent.adapters.AllPropertyListAdapter
import com.illopen.agent.databinding.FragmentAllPropertyListBinding
import com.illopen.agent.model.AvailableJobs
import com.illopen.agent.model.MyPostedJobList
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.ui.activities.JobsPropertyOnMap
import com.illopen.agent.ui.activities.MyPostedJobDetails
import com.illopen.agent.ui.activities.NewJobDetails
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.polyak.iconswitch.IconSwitch
import kotlinx.android.synthetic.main.fragment_all_property_list.*
import kotlinx.coroutines.*
import java.text.NumberFormat
import java.util.*

class AllPropertyListFragment : Fragment(R.layout.fragment_all_property_list),
    AllPropertyListAdapter.OnNewJobsClick, AllMyPostedJobsAdapter.OnItemClickListener {

    private val TAG: String = "AllPropertyListFragment"
    private var _binding: FragmentAllPropertyListBinding? = null
    private val binding get() = _binding!!
    var otherNewJobs = true

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    lateinit var timePicker: TimePickerDialog
    lateinit var datePicker: DatePickerDialog

    private lateinit var selectedDate: String
    private lateinit var selectedTime: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAllPropertyListBinding.bind(view)

        binding.filter.setOnClickListener {
            showFilterDialog()
        }

        getAllAvailableJobs()

        binding.otherJobs.setOnClickListener {
            otherNewJobs = true
            binding.otherJobs.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_users_tabbar)
            binding.myJobs.setBackgroundColor(Color.TRANSPARENT)
            getAllAvailableJobs()
        }

        binding.myJobs.setOnClickListener {
            otherNewJobs = false
            binding.otherJobs.setBackgroundColor(Color.TRANSPARENT)
            binding.myJobs.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_users_tabbar)
            getMyPostedJobs()
        }

        switchListToMap()
    }

    private fun switchListToMap() {
        binding.iconSwitch.setCheckedChangeListener { current ->
            when (current?.name) {
                IconSwitch.Checked.RIGHT.toString() -> {
                    Intent(requireContext(), JobsPropertyOnMap::class.java).apply {
                        startActivity(this)
                    }
                }
                else -> {
                    getAllAvailableJobs()
                }
            }
        }
    }

    private fun getMyPostedJobs() {
        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getNewMyPostedJobs(
                    AppPreferences.getUserData(Params.UserId).toInt(), 1, false, map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        val list: List<MyPostedJobList> = response.body()!!.values!!
                        if (list.isNotEmpty()) {
                            binding.upcomingJobs.adapter = AllMyPostedJobsAdapter(
                                Params.MY_POSTED_JOBS,
                                list,
                                this@AllPropertyListFragment
                            )
                        } else {
                            // no jobs found
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

    private fun getAllAvailableJobs() {
        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"
                val response = ServiceApi.retrofitService.getNewAvailableJobs(
                    AppPreferences.getUserData(Params.UserId).toInt(), 1, true, map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Log.d("getAvailableJobs", response.code().toString())
                        Log.d("getAvailableJobs", response.body().toString())

                        val list: List<AvailableJobs> = response.body()!!.values!!
                        binding.upcomingJobs.adapter = AllPropertyListAdapter(Params.OTHER_NEW_JOBS,
                                list, this@AllPropertyListFragment)
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

    @SuppressLint("CutPasteId")
    private fun showFilterDialog() {
        val mDialog = Dialog(requireContext(), R.style.Theme_PropertyMainBroker)
        mDialog.setContentView(R.layout.filter_layout)
        mDialog.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

//        val rangeSlider = mDialog.findViewById<RangeSlider>(R.id.rangeSlider)
        val startDate = mDialog.findViewById<TextView>(R.id.startDate)
        val endDate = mDialog.findViewById<TextView>(R.id.endDate)
        val startTime = mDialog.findViewById<TextView>(R.id.timeFrom)
        val endTime = mDialog.findViewById<TextView>(R.id.timeTo)
        val filter = mDialog.findViewById<Button>(R.id.searchFilter)


        startDate.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
//            val picker = builder.build()
//            val now = Calendar.getInstance()
//            builder.setSelection(now.timeInMillis)
//            picker.show(requireActivity().supportFragmentManager, picker.toString())

            val now = Calendar.getInstance()
            val year = now.get(Calendar.YEAR)
            val month = now.get(Calendar.MONTH)
            val day = now.get(Calendar.DAY_OF_MONTH)

            datePicker = DatePickerDialog(
                requireContext(),
                { _, _, monthOfYear, dayOfMonth ->
                    selectedDate =
                        year.toString() + "/" + (monthOfYear + 1).toString() + "/" + dayOfMonth.toString()
                            startDate.text = selectedDate
                },
                year,
                month,
                day
            )
            datePicker.show()
        }

        endDate.setOnClickListener {
//            val builder = MaterialDatePicker.Builder.datePicker()
//            val picker = builder.build()
//            val now = Calendar.getInstance()
//            builder.setSelection(now.timeInMillis)
//            picker.show(requireActivity().supportFragmentManager, picker.toString())

            val now = Calendar.getInstance()
            val year = now.get(Calendar.YEAR)
            val month = now.get(Calendar.MONTH)
            val day = now.get(Calendar.DAY_OF_MONTH)

            datePicker = DatePickerDialog(
                requireContext(),
                { _, _, monthOfYear, dayOfMonth ->
                    selectedDate =
                        year.toString() + "/" + (monthOfYear + 1).toString() + "/" + dayOfMonth.toString()
                    endDate.text = selectedDate
                },
                year,
                month,
                day
            )
            datePicker.show()
        }

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

    private fun searchDataAPI(jobNo : String) {
        coroutineScope.launch {
            try {
                val data = HashMap<String,String>()
                data["JobNo"] = jobNo
//                data["VisitDateFrom"] = ""
//                data["VisitDateTo"] = ""
//                data["VisitTimeFrom"] = ""
//                data["VisitTimeTo"] = ""
                val response = ServiceApi.retrofitService.searchNewJob(false,jobNo,data)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("searchData", response.code().toString())
                        Log.d("searchData", response.body().toString())

                        val list: List<AvailableJobs> = response.body()!!.values!!
                        Log.d(TAG, "searchDataAPI: ${Gson().toJson(list)}")
                         binding.upcomingJobs.adapter = AllPropertyListAdapter(
                                Params.OTHER_NEW_JOBS, list,this@AllPropertyListFragment)
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

    override fun onNewJobsClick(position: Int, currentItem: AvailableJobs) {
        val intent = Intent(activity, NewJobDetails::class.java)
        intent.putExtra("JobData", currentItem.id.toString())
        intent.putExtra(Params.SUB_FROM, Params.OTHER_NEW_JOBS)
        startActivity(intent)
    }

    override fun onItemClick(itemPosition: Int, data: MyPostedJobList) {
        val intent = Intent(activity, MyPostedJobDetails::class.java)
        intent.putExtra("PostData", data.id.toString())
        intent.putExtra(Params.SUB_FROM, Params.MY_POSTED_JOBS)
        startActivity(intent)
    }

    override fun onCallClick(itemPosition: Int, data: MyPostedJobList) {
        Intent(Intent.ACTION_CALL).apply {
            setData(Uri.parse(data.userPhoneNumber.toString()))
            startActivity(this)
        }
//        val intent = Intent(Intent.ACTION_CALL)
//        intent.data = Uri.parse(data.userPhoneNumber)
//        requireActivity().startActivity(intent)
    }

}