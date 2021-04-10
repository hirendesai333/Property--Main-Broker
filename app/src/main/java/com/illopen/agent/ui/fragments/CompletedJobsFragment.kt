package com.illopen.agent.ui.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
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
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.agent.model.CompletedJobsAssignList
import com.illopen.agent.model.CompletedMyPostedJobsList
import com.illopen.agent.ui.activities.CompletedJobAssignDetails
import com.illopen.agent.ui.activities.CompletedJobDetails
import com.illopen.properybroker.utils.toast
import kotlinx.coroutines.*
import java.text.NumberFormat
import java.util.*


class CompletedJobsFragment : Fragment(R.layout.completed_jobs_fragment), CompletedJobsAdapter.OnCompletedJobClick
    ,MyCompletedJobsAdapter.OnCompletedAssignClickListener, CompletedJobsAdapter.OnMarkerClick {

    private var _binding: CompletedJobsFragmentBinding? = null
    private val binding get() = _binding!!
    private var otherNewJobs = true

    private val TAG : String = "CompletedJobsFragment"

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    lateinit var timePicker: TimePickerDialog
    lateinit var datePicker: DatePickerDialog

    private lateinit var selectedDate: String
    private lateinit var selectedTime: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = CompletedJobsFragmentBinding.bind(view)

          getAssignedJob()

        binding.otherJobs.setOnClickListener {
            otherNewJobs = true
            binding.otherJobs.background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_users_tabbar)
            binding.myJobs.setBackgroundColor(Color.TRANSPARENT)
            getAssignedJob()
        }

        binding.myJobs.setOnClickListener {
            otherNewJobs = false
            binding.otherJobs.setBackgroundColor(Color.TRANSPARENT)
            binding.myJobs.background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_users_tabbar)
            getMyPostedCompletedJobs()
        }

        binding.search.setOnClickListener {
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
                    3,2,map
                )
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("MY_COMPLETED_JOBS", response.code().toString())
                        Log.d("MY_COMPLETED_JOBS",response.body().toString())

                        val list : List<CompletedJobsAssignList> = response.body()!!.values!!

                        binding.completedJobRv.adapter = MyCompletedJobsAdapter(Params.MY_COMPLETED_JOBS, list,this@CompletedJobsFragment)

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
                    AppPreferences.getUserData(Params.UserId).toInt(),3,false,map
                )
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getCompletedJobs", response.code().toString())
                        Log.d("getCompletedJobs",response.body().toString())

                        val list : List<CompletedMyPostedJobsList> = response.body()!!.values!!

                        binding.completedJobRv.adapter = CompletedJobsAdapter(Params.MY_POSTED_COMPLETED_JOBS,list,this@CompletedJobsFragment,
                            this@CompletedJobsFragment)
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
                val response = ServiceApi.retrofitService.searchCompleteJob(false,jobNo,data)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("searchData", response.code().toString())
                        Log.d("searchData", response.body().toString())

                        val list : List<CompletedMyPostedJobsList> = response.body()!!.values!!

                        binding.completedJobRv.adapter = CompletedJobsAdapter(
                            Params.MY_POSTED_COMPLETED_JOBS,list,
                            this@CompletedJobsFragment,
                            this@CompletedJobsFragment)
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
//        Intent(activity, CompletedJobDetails::class.java).apply {
//            putExtra("CompletedJob", currentItem.id.toString())
//            putExtra(Params.FROM, Params.COMPLETED_JOBS_FRAGMENT)
//            if (otherNewJobs) {
//                putExtra(Params.SUB_FROM, Params.MY_COMPLETED_JOBS)
//            } else {
//                putExtra(Params.SUB_FROM, Params.MY_POSTED_COMPLETED_JOBS)
//            }
//            startActivity(this)
//        }

        val intent = Intent(activity, CompletedJobDetails::class.java)
        intent.putExtra("CompletedMyPostedJob",currentItem.id.toString())
        intent.putExtra(Params.SUB_FROM,Params.MY_POSTED_COMPLETED_JOBS)
        startActivity(intent)
    }

    override fun onItemClick(itemPosition: Int, data: CompletedJobsAssignList) {
        val intent = Intent(activity, CompletedJobAssignDetails::class.java)
        intent.putExtra("CompletedAssignJob",data.id.toString())
        intent.putExtra(Params.SUB_FROM,Params.MY_COMPLETED_JOBS)
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
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("Mark Completed Property", response.code().toString())
                        Log.d("Mark CompletedProperty", response.body().toString())
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

}