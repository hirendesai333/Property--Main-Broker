package com.illopen.agent.ui.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.slider.RangeSlider
import com.illopen.agent.R
import com.illopen.agent.adapters.CompletedJobsAdapter
import com.illopen.agent.adapters.MyCompletedJobsAdapter
import com.illopen.agent.databinding.CompletedJobsFragmentBinding
import com.illopen.agent.model.AllCompletedJobsList
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.agent.listeners.OnCompletedJobClick
import com.illopen.agent.model.CompletedJobsAssignList
import com.illopen.agent.ui.activities.CompletedJobDetails
import kotlinx.coroutines.*
import java.text.NumberFormat
import java.util.*


class CompletedJobsFragment : Fragment(R.layout.completed_jobs_fragment), OnCompletedJobClick , MyCompletedJobsAdapter.OnItemClickListener {

    private var _binding: CompletedJobsFragmentBinding? = null
    private val binding get() = _binding!!
    private var otherNewJobs = true

    private val TAG : String = "CompletedJobsFragment"

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

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
            getAllCompletedJobs()
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

                val response = ServiceApi.retrofitService.getCompletedJobs(
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


    private fun getAllCompletedJobs() {
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

                        val list : List<AllCompletedJobsList> = response.body()!!.values!!

                        binding.completedJobRv.adapter = CompletedJobsAdapter(Params.MY_POSTED_COMPLETED_JOBS,list,this@CompletedJobsFragment)
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

    private fun showFilterDialog() {
        val mDialog = Dialog(requireContext(), R.style.Theme_PropertyMainBroker)
        mDialog.setContentView(R.layout.filter_layout)
        mDialog.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        val rangeSlider = mDialog.findViewById<RangeSlider>(R.id.rangeSlider)
        val startDate = mDialog.findViewById<TextView>(R.id.startDate)
        val endDate = mDialog.findViewById<TextView>(R.id.endDate)
        val filter = mDialog.findViewById<Button>(R.id.filter)

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

        rangeSlider.setLabelFormatter { value: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("USD")
            format.format(value.toDouble())
        }

        filter.setOnClickListener {
            mDialog.dismiss()
        }

        mDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCompletedJobsClick(currentItem: AllCompletedJobsList) {
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
        intent.putExtra("CompletedJob",currentItem.id.toString())
        intent.putExtra(Params.SUB_FROM,Params.MY_POSTED_COMPLETED_JOBS)
        startActivity(intent)
    }

    override fun onItemClick(itemPosition: Int, data: CompletedJobsAssignList) {
        val intent = Intent(activity, CompletedJobDetails::class.java)
        intent.putExtra("CompletedJob",data.id.toString())
        intent.putExtra(Params.SUB_FROM,Params.MY_COMPLETED_JOBS)
        startActivity(intent)
    }

}