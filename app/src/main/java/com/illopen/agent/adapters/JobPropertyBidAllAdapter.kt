package com.illopen.agent.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.databinding.JobPropertyAllBidListBinding
import com.illopen.agent.model.JobBidList

class JobPropertyBidAllAdapter(var context: Context, var list: List<JobBidList>
    ,var itemClickListener : OnItemClickListener) :
    RecyclerView.Adapter<JobPropertyBidAllAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.job_property_all_bid_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.binding.apply {
            name.text = currentItem.userName.toString()
            email.text = "Email: "+ currentItem.userEmail.toString()
            phone.text = "Phone Number: "+currentItem.userPhoneNumber.toString()
            price.text = "Total Amount: $" + currentItem.totalAmount.toString()
            totalProperty.text = "Total Bid Property: ${currentItem.totalPropertyBid}/${currentItem.totalProperty}"
        }

        holder.binding.agentAssign.setOnClickListener {
            itemClickListener.onAssignClick(position, list[position])
        }

        holder.binding.allBidPropertyShow.setOnClickListener {
           itemClickListener.onEyePropertyClick(position,list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = JobPropertyAllBidListBinding.bind(itemView)
    }

    interface OnItemClickListener {
        fun onAssignClick(itemPosition: Int, data: JobBidList)
        fun onEyePropertyClick(itemPosition: Int, data: JobBidList)
    }
}