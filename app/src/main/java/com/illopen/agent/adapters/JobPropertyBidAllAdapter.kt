package com.illopen.agent.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.databinding.JobPropertyAllBidListBinding
import com.illopen.agent.model.JobBidValue

class JobPropertyBidAllAdapter(var context: Context, var list: List<JobBidValue>) :
    RecyclerView.Adapter<JobPropertyBidAllAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.job_property_all_bid_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val currentItem = list[position]
            name.text = currentItem.amount.toString()
            propertyType.text = currentItem.note.toString()
            add.text = currentItem.propertyAddress.toString()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = JobPropertyAllBidListBinding.bind(itemView)
    }
}