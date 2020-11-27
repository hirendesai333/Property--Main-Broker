package com.soboft.propertybroker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.soboft.propertybroker.R
import com.soboft.propertybroker.adapters.CompletedJobsAdapter
import com.soboft.propertybroker.databinding.CompletedJobsFragmentBinding
import com.soboft.propertybroker.model.PropertyListModel
import com.soboft.properybroker.listeners.OnPropertyClick

class CompletedJobsFragment : Fragment(R.layout.completed_jobs_fragment), OnPropertyClick {

    private var _binding: CompletedJobsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = CompletedJobsFragmentBinding.bind(view)

        val list = ArrayList<PropertyListModel>()
        list.add(PropertyListModel("Shivalik Shilp"))
        list.add(PropertyListModel("Aditya Prime"))
        list.add(PropertyListModel("Saujanya 2"))
        binding.completedJobRv.adapter = CompletedJobsAdapter(list, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPropertyClick() {

    }

}