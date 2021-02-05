package com.soboft.propertybroker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soboft.propertybroker.R
import com.soboft.propertybroker.databinding.JobPropertyLtemBinding
import com.soboft.propertybroker.listeners.OnGoingJobPropertyClick
import com.soboft.propertybroker.model.JobPropertyList

class OnGoingJobAdapter(var context: Context,var list : List<JobPropertyList>, var onGoingJobPropertyClick: OnGoingJobPropertyClick)
    : RecyclerView.Adapter<OnGoingJobAdapter.ViewHolder>() {

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val binding = JobPropertyLtemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ongoing_job_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.binding
        val currentItem = list[position]

        view.name.text = currentItem.propertyName
        view.address.text = currentItem.propertyAddress
        view.propertyType.text = currentItem.propertyTypeName

        view.rootLayout.setOnClickListener {
            onGoingJobPropertyClick.onGoingJobPropertyClick(currentItem)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}