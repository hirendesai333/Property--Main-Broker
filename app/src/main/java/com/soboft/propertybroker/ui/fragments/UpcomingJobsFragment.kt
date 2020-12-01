package com.soboft.propertybroker.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.soboft.propertybroker.R
import com.soboft.propertybroker.adapters.AllPropertyListAdapter
import com.soboft.propertybroker.adapters.UpcomingBidsAdapter
import com.soboft.propertybroker.databinding.FragmentUpcomingJobsBinding
import com.soboft.propertybroker.model.PropertyListModel
import com.soboft.propertybroker.ui.activities.PropertyDetail
import com.soboft.propertybroker.utils.Params
import com.soboft.properybroker.listeners.OnPropertyClick

class UpcomingJobsFragment : Fragment(R.layout.fragment_upcoming_jobs), OnPropertyClick {

    private var _binding: FragmentUpcomingJobsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUpcomingJobsBinding.bind(view)

        val list = ArrayList<PropertyListModel>()
        list.add(PropertyListModel("Shivalik Shilp"))
        list.add(PropertyListModel("Aditya Prime"))
        list.add(PropertyListModel("Saujanya 2"))
        binding.upcomingJobsRv.adapter = UpcomingBidsAdapter(list, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPropertyClick() {
        Intent(activity, PropertyDetail::class.java).apply {
            putExtra(Params.FROM, Params.ONGOING_JOBS_FRAGMENT)
            startActivity(this)
        }
    }
}