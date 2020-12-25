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
import com.soboft.propertybroker.adapters.AllPropertyListAdapter
import com.soboft.propertybroker.databinding.FragmentAllPropertyListBinding
import com.soboft.propertybroker.model.PropertyListModel
import com.soboft.propertybroker.ui.activities.PropertyDetail
import com.soboft.propertybroker.utils.Params
import com.soboft.properybroker.listeners.OnPropertyClick
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AllPropertyListFragment : Fragment(R.layout.fragment_all_property_list), OnPropertyClick {

    private var _binding: FragmentAllPropertyListBinding? = null
    private val binding get() = _binding!!
    val list = ArrayList<PropertyListModel>()
    var otherNewJobs = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAllPropertyListBinding.bind(view)

        list.add(PropertyListModel("Shivalik Shilp", isBidAdded = 1))
        list.add(PropertyListModel("Aditya Prime", isBidAdded = 0))
        list.add(PropertyListModel("Saujanya 2", isBidAdded = 0))
        binding.upcomingJobs.adapter = AllPropertyListAdapter(Params.OTHER_NEW_JOBS, list, this)

        binding.filter.setOnClickListener {
            showFilterDialog()
        }

        binding.otherJobs.setOnClickListener {
            otherNewJobs = true
            binding.otherJobs.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_users_tabbar)
            binding.myJobs.setBackgroundColor(Color.TRANSPARENT)
            binding.upcomingJobs.adapter = AllPropertyListAdapter(Params.OTHER_NEW_JOBS, list, this)
        }

        binding.myJobs.setOnClickListener {
            otherNewJobs = false
            binding.otherJobs.setBackgroundColor(Color.TRANSPARENT)
            binding.myJobs.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_users_tabbar)
            binding.upcomingJobs.adapter = AllPropertyListAdapter(Params.MY_POSTED_JOBS, list, this)
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
            putExtra(Params.FROM, Params.ALL_PROPERTY_LIST_FRAGMENT)
            if (otherNewJobs) {
                putExtra(Params.SUB_FROM, Params.OTHER_NEW_JOBS)
            } else {
                putExtra(Params.SUB_FROM, Params.MY_POSTED_JOBS)
            }
            startActivity(this)
        }
    }
}