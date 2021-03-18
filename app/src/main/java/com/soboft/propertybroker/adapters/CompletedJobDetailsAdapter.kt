package com.soboft.propertybroker.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soboft.propertybroker.R
import com.soboft.propertybroker.databinding.CompletedJobItemBinding
import com.soboft.propertybroker.databinding.JobPropertyLtemBinding
import com.soboft.propertybroker.listeners.OnCompletedJobClick
import com.soboft.propertybroker.listeners.OnCompletedJobPropertyClick
import com.soboft.propertybroker.model.JobPropertyList
import com.soboft.propertybroker.ui.activities.PropertyDetail

class CompletedJobDetailsAdapter(var context: Context,var list: List<JobPropertyList>, var onCompletedJobPropertyClick: OnCompletedJobPropertyClick)
    : RecyclerView.Adapter<CompletedJobDetailsAdapter.ViewHolder>() {

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val binding = CompletedJobItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.completed_job_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.binding
        val currentItem = list[position]

        view.name.text = currentItem.propertyName
        view.address.text = currentItem.propertyAddress
        view.propertyType.text = currentItem.propertyTypeName

        view.rootLayout.setOnClickListener {
            val intent =  Intent(context, PropertyDetail::class.java)
            intent.putExtra("propertyMasterId",currentItem.propertyMasterId.toString())
            context.startActivity(intent)
        }

        view.markCompleted.setOnClickListener {
            onCompletedJobPropertyClick.onCompletedJobPropertyClick(currentItem)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}