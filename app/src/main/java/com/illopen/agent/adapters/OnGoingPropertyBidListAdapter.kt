package com.illopen.agent.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.databinding.JobPropertyAllBidListBinding
import com.illopen.agent.databinding.OnGoingBidItemListBinding
import com.illopen.agent.model.JobBidList

class OnGoingPropertyBidListAdapter(var context: Context, var list: List<JobBidList>)
    : RecyclerView.Adapter<OnGoingPropertyBidListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.on_going_bid_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val currentItem = list[position]
            name.text = currentItem.userName.toString()
            price.text = "Total Amount: $" + currentItem.totalAmount.toString()
            totalProperty.text = "Total Bid Property: ${currentItem.totalPropertyBid}/${currentItem.totalProperty}"
            email.text = "Email: " + currentItem.userEmail
            phone.text = "Phone Number: " + currentItem.userPhoneNumber

        }
    }

    override fun getItemCount(): Int {
       return  list.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val binding = OnGoingBidItemListBinding.bind(itemView)
    }
}