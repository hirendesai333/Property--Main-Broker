package com.soboft.propertybroker.ui.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.slider.RangeSlider
import com.soboft.propertybroker.R
import com.soboft.propertybroker.adapters.UpcomingBidsAdapter
import com.soboft.propertybroker.databinding.FragmentUpcomingJobsBinding
import com.soboft.propertybroker.model.PropertyListModel
import com.soboft.propertybroker.ui.activities.PropertyDetail
import com.soboft.propertybroker.utils.Params
import com.soboft.properybroker.listeners.OnPropertyClick
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class UpcomingJobsFragment : Fragment(R.layout.fragment_upcoming_jobs), OnPropertyClick {

    private var _binding: FragmentUpcomingJobsBinding? = null
    private val binding get() = _binding!!
    val list = ArrayList<PropertyListModel>()
    private var otherNewJobs = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUpcomingJobsBinding.bind(view)

        list.add(PropertyListModel("Shivalik Shilp", jobStatus = 0))
        list.add(PropertyListModel("Aditya Prime", jobStatus = 1))
        list.add(PropertyListModel("Saujanya 2", jobStatus = 0))

        binding.upcomingJobsRv.adapter = UpcomingBidsAdapter(Params.JOB_ASSIGN_TO_ME, list, this)

        binding.filter.setOnClickListener {
            showFilterDialog()
        }

        binding.assginedobs.setOnClickListener {
            otherNewJobs = true
            binding.assginedobs.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_users_tabbar)
            binding.postedJobs.setBackgroundColor(Color.TRANSPARENT)
            binding.upcomingJobsRv.adapter =
                UpcomingBidsAdapter(Params.JOB_ASSIGN_TO_ME, list, this)
        }

        binding.postedJobs.setOnClickListener {
            otherNewJobs = false
            binding.assginedobs.setBackgroundColor(Color.TRANSPARENT)
            binding.postedJobs.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_users_tabbar)
            binding.upcomingJobsRv.adapter =
                UpcomingBidsAdapter(Params.MY_POSTED_ONGOING_JOBS, list, this)
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

    override fun onPropertyClick(currentItem: PropertyListModel) {
        Intent(activity, PropertyDetail::class.java).apply {
            putExtra("JobData", currentItem)
            putExtra(Params.FROM, Params.ONGOING_JOBS_FRAGMENT)
            if (otherNewJobs) {
                putExtra(Params.SUB_FROM, Params.JOB_ASSIGN_TO_ME)
            } else {
                putExtra(Params.SUB_FROM, Params.MY_POSTED_ONGOING_JOBS)
            }
            startActivity(this)
        }
    }
}