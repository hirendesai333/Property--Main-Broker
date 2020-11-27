package com.soboft.propertybroker.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.soboft.propertybroker.R
import com.soboft.propertybroker.adapters.AllPropertyListAdapter
import com.soboft.propertybroker.databinding.FragmentAllPropertyListBinding
import com.soboft.propertybroker.model.PropertyListModel
import com.soboft.propertybroker.ui.activities.PropertyDetail
import com.soboft.properybroker.listeners.OnPropertyClick

class AllPropertyListFragment : Fragment(R.layout.fragment_all_property_list), OnPropertyClick {

    private var _binding: FragmentAllPropertyListBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAllPropertyListBinding.bind(view)

        val list = ArrayList<PropertyListModel>()
        list.add(PropertyListModel("Shivalik Shilp"))
        list.add(PropertyListModel("Aditya Prime"))
        list.add(PropertyListModel("Saujanya 2"))
        binding.upcomingJobs.adapter = AllPropertyListAdapter(list, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPropertyClick() {
        startActivity(Intent(activity, PropertyDetail::class.java))
    }
}