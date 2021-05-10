package com.illopen.agent.ui.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
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
import com.google.android.gms.maps.*
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
import com.illopen.agent.ui.activities.Notification
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.agent.utils.ProgressDialog
import com.illopen.properybroker.utils.toast
import com.polyak.iconswitch.IconSwitch
import kotlinx.android.synthetic.main.fragment_all_property_list.*
import kotlinx.coroutines.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AllPropertyListFragment : Fragment(R.layout.fragment_all_property_list),
    AllPropertyListAdapter.OnNewJobsClick, AllMyPostedJobsAdapter.OnItemClickListener {

    private val TAG: String = "AllPropertyListFragment"
    private var _binding: FragmentAllPropertyListBinding? = null
    private val binding get() = _binding!!
    var otherNewJobs = true

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    private var startDateSelect: String = ""
    private var endDateSelect: String = ""
    private var startTimeSelect: String = ""
    private var endTimeSelect: String = ""

    private lateinit var progressDialog: ProgressDialog

    private lateinit var availableJobAdapter: AllPropertyListAdapter
    private lateinit var postedJobAdapter: AllMyPostedJobsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAllPropertyListBinding.bind(view)

        progressDialog = ProgressDialog(requireContext())

        binding.filter.setOnClickListener {
            showFilterDialog()
        }

        binding.notification.setOnClickListener {
            Intent(requireContext(),Notification::class.java).apply {
                startActivity(this)
            }
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

    private fun availableFilterList(filterItem: String, list: List<AvailableJobs>) {

        val tempList: ArrayList<AvailableJobs> = ArrayList()

        for (i in list) {

            if (filterItem in i.jobNo.toString() || filterItem.toLowerCase() in i.customerName!!.toLowerCase()) {
                tempList.add(i)
            }
        }
        availableJobAdapter.updateList(tempList)

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

                        val list: ArrayList<MyPostedJobList> = response.body()!!.values!!
                        postedJobAdapter = AllMyPostedJobsAdapter(
                            Params.MY_POSTED_JOBS, list, this@AllPropertyListFragment
                        )
                        binding.upcomingJobs.adapter = postedJobAdapter

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
                Log.d(TAG, e.message.toString())
            }
        }
    }

    private fun postedFilterList(filterItem: String, list: List<MyPostedJobList>) {
        val tempList: ArrayList<MyPostedJobList> = ArrayList()

        for (i in list) {

            if (filterItem in i.jobNo.toString() || filterItem.toLowerCase() in i.customerName!!.toLowerCase()) {
                tempList.add(i)
            }
        }
        postedJobAdapter.updateList(tempList)
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


                        val list: ArrayList<AvailableJobs> = response.body()!!.values!!
                        availableJobAdapter = AllPropertyListAdapter(
                            Params.OTHER_NEW_JOBS,
                            list, this@AllPropertyListFragment
                        )
                        binding.upcomingJobs.adapter = availableJobAdapter


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
                                    availableFilterList(s.toString(), list)
                                } else {
                                    availableJobAdapter.updateList(list)
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

//            val now = Date()
//            val calendar = Calendar.getInstance()
//            calendar.time = Date(now.time)
//            val date = calendar.time
//            SingleDateAndTimePickerDialog.Builder(activity)
//                .defaultDate(date)
//                .mustBeOnFuture()
//                .curved()
//                .displayMinutes(false)
//                .displayHours(false)
//                .displayMonth(true)
//                .displayYears(true)
//                .displayDaysOfMonth(true)
//                .displayMonthNumbers(true)
//                .displayDays(false)
//                .title("Select Date")
//                .listener {
//                    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
//                    startDateSelect = dateFormat.format(it).toString()
//                    startDate.text = startDateSelect
//                }
//                .display()

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
//            val now = Date()
//            val calendar = Calendar.getInstance()
//            calendar.time = Date(now.time)
//            val date = calendar.time
//            SingleDateAndTimePickerDialog.Builder(requireContext())
//                .defaultDate(date)
//                .mustBeOnFuture()
//                .curved()
//                .displayMinutes(false)
//                .displayHours(false)
//                .displayMonth(true)
//                .displayYears(true)
//                .displayDaysOfMonth(true)
//                .displayMonthNumbers(true)
//                .displayDays(false)
//                .title("Select Date")
//                .listener {
//                    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
//                    endDateSelect = dateFormat.format(it).toString()
//                    endDate.text = endDateSelect
//                }
//                .display()

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

        startTime.setOnClickListener {

//            SingleDateAndTimePickerDialog.Builder(requireContext())
//                .curved()
//                .displayMinutes(true)
//                .displayHours(true)
//                .displayMinutes(true)
//                .displayMonth(false)
//                .displayYears(false)
//                .displayDaysOfMonth(false)
//                .displayMonthNumbers(false)
//                .displayDays(false)
//                .title("Select time")
//                .listener {
//                    val dateFormat: DateFormat = SimpleDateFormat("hh:mm aa", Locale.US)
//                    startTimeSelect = dateFormat.format(it).toString()
//                    startTime.text = startTimeSelect
//                }
//                .display()

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

                    val dateFormat : String = timeFormatAmPm.format(datetime.time)
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

//            SingleDateAndTimePickerDialog.Builder(requireContext())
//                .curved()
//                .displayMinutes(true)
//                .displayHours(true)
//                .displayMinutes(true)
//                .displayMonth(false)
//                .displayYears(false)
//                .displayDaysOfMonth(false)
//                .displayMonthNumbers(false)
//                .displayDays(false)
//                .title("Select time")
//                .listener {
//                    val dateFormat: DateFormat = SimpleDateFormat("hh:mm aa", Locale.US)
//                    endTimeSelect = dateFormat.format(it).toString()
//                    endTime.text = endTimeSelect
//                }
//                .display()

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

                    val dateFormat : String = timeFormatAmPm.format(datetime.time)
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
                searchAvailableDataAPI()
                searchPostedDataAPI()
                mDialog.dismiss()
            }
        }
        mDialog.show()
    }

    private fun searchPostedDataAPI() {
        progressDialog.dialog.show()
        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.searchNewPostedJob(1,false,startDateSelect,
                    endDateSelect,startTimeSelect,endTimeSelect,map)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("searchData", response.code().toString())
                        Log.d("searchData", response.body().toString())

                        if (response.code() == 200){
                            requireActivity().toast("Searching..")
                        }else{
                            requireActivity().toast("No data found")
                        }
                        val list: ArrayList<AvailableJobs> = response.body()!!.values!!
                        binding.upcomingJobs.adapter = AllPropertyListAdapter(
                            Params.OTHER_NEW_JOBS, list, this@AllPropertyListFragment)

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

    private fun searchAvailableDataAPI() {
        progressDialog.dialog.show()
        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.searchNewJob(false,startDateSelect,
                    endDateSelect,startTimeSelect,endTimeSelect,map)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("searchData", response.code().toString())
                        Log.d("searchData", response.body().toString())

                        if (response.code() == 200){
                            requireActivity().toast("Searching..")
                        }else{
                            requireActivity().toast("No data found")
                        }
                        val list: ArrayList<AvailableJobs> = response.body()!!.values!!
                        binding.upcomingJobs.adapter = AllPropertyListAdapter(
                            Params.OTHER_NEW_JOBS, list, this@AllPropertyListFragment)

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