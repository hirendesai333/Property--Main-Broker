package com.illopen.agent.ui.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.slider.RangeSlider
import com.google.gson.Gson
import com.illopen.agent.R
import com.illopen.agent.adapters.AllMyPostedJobsAdapter
import com.illopen.agent.adapters.AllPropertyListAdapter
import com.illopen.agent.databinding.FragmentAllPropertyListBinding
import com.illopen.agent.model.AvailableJobs
import com.illopen.agent.model.MapDetailsList
import com.illopen.agent.model.MyPostedJobsList
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
                        val list: List<MyPostedJobsList> = response.body()!!.values!!
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
                        if (list.size > 0) {
                            binding.upcomingJobs.adapter = AllPropertyListAdapter(
                                Params.OTHER_NEW_JOBS,
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

    override fun onNewJobsClick(position: Int, currentItem: AvailableJobs) {
        val intent = Intent(activity, NewJobDetails::class.java)
        intent.putExtra("JobData", currentItem.id.toString())
        intent.putExtra(Params.SUB_FROM, Params.OTHER_NEW_JOBS)
        startActivity(intent)
    }

    override fun onItemClick(itemPosition: Int, data: MyPostedJobsList) {
        val intent = Intent(activity, MyPostedJobDetails::class.java)
        intent.putExtra("PostData", data.id.toString())
        intent.putExtra(Params.SUB_FROM, Params.MY_POSTED_JOBS)
        startActivity(intent)
    }

    override fun onCallClick(itemPosition: Int, data: MyPostedJobsList) {
        Intent(Intent.ACTION_CALL).apply {
            setData(Uri.parse(data.userPhoneNumber))
            startActivity(this)
        }
    }

}