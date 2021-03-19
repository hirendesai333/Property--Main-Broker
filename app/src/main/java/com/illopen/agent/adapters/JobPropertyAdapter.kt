package com.illopen.agent.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.listeners.OnJobPropertyClick
import com.illopen.agent.model.JobPropertyList
import com.illopen.agent.ui.activities.PropertyDetail

class JobPropertyAdapter(var context: Context,var list : List<JobPropertyList> , var onJobPropertyClick: OnJobPropertyClick)
    : RecyclerView.Adapter<JobPropertyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.job_property_ltem,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.name.text = currentItem.propertyName
        holder.address.text = "Address: " + currentItem.propertyAddress
        holder.propertyType.text = "Type: " + currentItem.propertyTypeName
        holder.price.text = "Price: $" + currentItem.propertyPrice
        holder.bid.setOnClickListener {
            onJobPropertyClick.onJobPropertyClick(currentItem)
        }
        if (currentItem.bidAmount!! > 0) {
            holder.bid.text = "Update Bid"
        } else {
            holder.bid.text = "Make Bid"
        }

        holder.rootLayout.setOnClickListener {
           val intent =  Intent(context, PropertyDetail::class.java)
            intent.putExtra("propertyMasterId", currentItem.propertyMasterId.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val address: TextView = itemView.findViewById(R.id.address)
        val propertyType :TextView = itemView.findViewById(R.id.propertyType)
        val price :TextView = itemView.findViewById(R.id.price)
        val bid : Button = itemView.findViewById(R.id.bidding)
        val rootLayout : CardView = itemView.findViewById(R.id.rootLayout)
    }
}