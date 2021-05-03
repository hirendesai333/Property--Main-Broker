package com.illopen.agent.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.databinding.CompletedMypostedJobBidItemBinding
import com.illopen.agent.model.JobBidList

class CompletedMyPostedJobBidAdapter(var context: Context,var list: List<JobBidList>,var itemClick : OnPropertyShowClick)
    : RecyclerView.Adapter<CompletedMyPostedJobBidAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.completed_myposted_job_bid_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val currentItem = list[position]
            name.text = currentItem.userName.toString()
            price.text = "Bid Amount: $" + currentItem.totalAmount.toString()
            email.text = "Email: " + currentItem.userEmail
            phone.text = "Phone Number: " + currentItem.userPhoneNumber
            totalProperty.text = "Total Bid Property: ${currentItem.totalPropertyBid}/${currentItem.totalProperty}"
        }

        holder.binding.allBidPropertyShow.setOnClickListener {
            itemClick.onEyePropertyClick(position,list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val binding = CompletedMypostedJobBidItemBinding.bind(itemView)
    }

    interface OnPropertyShowClick{
        fun onEyePropertyClick(itemPosition: Int, data: JobBidList)
    }

}