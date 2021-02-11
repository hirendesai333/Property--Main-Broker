package com.soboft.propertybroker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soboft.propertybroker.R
import com.soboft.propertybroker.databinding.JobPropertyAllBidListBinding
import com.soboft.propertybroker.model.JobPropertyBidAllList

class JobPropertyBidAllAdapter(context: Context,var list: List<JobPropertyBidAllList>)
    : RecyclerView.Adapter<JobPropertyBidAllAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.job_property_all_bid_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val itemView = holder.binding
       val currentItem = list[position]

        itemView.name.text = currentItem.amount.toString()
        itemView.propertyType.text = currentItem.note.toString()
        itemView.address.text = currentItem.propertyAddress
    }

    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val binding = JobPropertyAllBidListBinding.bind(itemView)
    }
}