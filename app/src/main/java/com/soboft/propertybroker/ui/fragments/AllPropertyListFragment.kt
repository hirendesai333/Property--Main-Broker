package com.soboft.propertybroker.ui.fragments

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
import com.soboft.propertybroker.R
import com.soboft.propertybroker.adapters.AllPropertyListAdapter
import com.soboft.propertybroker.databinding.FragmentAllPropertyListBinding
import com.soboft.propertybroker.model.AvailableJobs
import com.soboft.propertybroker.network.ServiceApi
import com.soboft.propertybroker.ui.activities.PropertyDetail
import com.soboft.propertybroker.utils.AppPreferences
import com.soboft.propertybroker.utils.Params
import com.soboft.propertybroker.listeners.OnNewJobsClick
import com.soboft.propertybroker.ui.activities.NewJobDetails
import kotlinx.coroutines.*
import java.text.NumberFormat
import java.util.*

class AllPropertyListFragment : Fragment(R.layout.fragment_all_property_list), OnNewJobsClick {

    private val TAG : String = "AllPropertyListFragment"
    private var _binding: FragmentAllPropertyListBinding? = null
    private val binding get() = _binding!!
    var otherNewJobs = true

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAllPropertyListBinding.bind(view)

        getAllAvailableJobs()

        binding.filter.setOnClickListener {
            showFilterDialog()
        }

        binding.otherJobs.setOnClickListener {
            otherNewJobs = true
            binding.otherJobs.background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_users_tabbar)
            binding.myJobs.setBackgroundColor(Color.TRANSPARENT)
            getAllAvailableJobs()
        }

        binding.myJobs.setOnClickListener {
            otherNewJobs = false
            binding.otherJobs.setBackgroundColor(Color.TRANSPARENT)
            binding.myJobs.background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_users_tabbar)
            getMyPostedJobs()
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

                val response = ServiceApi.retrofitService.getNewJobs(
                    AppPreferences.getUserData(Params.UserId).toInt(),1,false,map
                )
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getMyPostedJobs", response.code().toString())
                        Log.d("getMyPostedJobs",response.body().toString())

                        val list : List<AvailableJobs> = response.body()!!.values!!

                        binding.upcomingJobs.adapter = AllPropertyListAdapter(Params.MY_POSTED_JOBS, list, this@AllPropertyListFragment)

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

    private fun getAllAvailableJobs() {
        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getNewJobs(
                    AppPreferences.getUserData(Params.UserId).toInt(),1,true,map
                )
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getAvailableJobs", response.code().toString())
                        Log.d("getAvailableJobs",response.body().toString())

                        val list : List<AvailableJobs> = response.body()!!.values!!

                        binding.upcomingJobs.adapter = AllPropertyListAdapter(Params.OTHER_NEW_JOBS, list, this@AllPropertyListFragment)

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

    override fun onNewJobsClick(currentItem: AvailableJobs) {
//        Intent(activity, NewJobDetails::class.java).apply {
//            putExtra("JobData", currentItem.id.toString())
//            putExtra(Params.FROM, Params.ALL_PROPERTY_LIST_FRAGMENT)
//            if (otherNewJobs) {
//                putExtra(Params.SUB_FROM, Params.OTHER_NEW_JOBS)
//            } else {
//                putExtra(Params.SUB_FROM, Params.MY_POSTED_JOBS)
//            }
//            startActivity(this)
//        }

        val intent = Intent(activity,NewJobDetails::class.java)
        intent.putExtra("JobData",currentItem.id.toString())
        intent.putExtra(Params.FROM,Params.ALL_PROPERTY_LIST_FRAGMENT)
        startActivity(intent)
    }
}