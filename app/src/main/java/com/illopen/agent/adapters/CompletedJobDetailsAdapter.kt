package com.illopen.agent.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.databinding.CompletedJobItemBinding
import com.illopen.agent.listeners.OnCompletedJobPropertyClick
import com.illopen.agent.model.JobPropertyList
import com.illopen.agent.ui.activities.PropertyDetail

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