package com.soboft.propertybroker.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.soboft.propertybroker.R
import com.soboft.propertybroker.listeners.OnJobPropertyClick
import com.soboft.propertybroker.model.JobPropertyList
import com.soboft.propertybroker.ui.activities.PropertyDetail

class JobPropertyAdapter(var context: Context,var list : List<JobPropertyList> , var onJobPropertyClick: OnJobPropertyClick)
    : RecyclerView.Adapter<JobPropertyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.job_property_ltem,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]

        holder.name.text = currentItem.propertyName
        holder.address.text = currentItem.propertyAddress
        holder.propertyType.text = currentItem.propertyTypeName

//        holder.name.setOnClickListener {
//            onJobPropertyClick.onJobPropertyClick(currentItem)
//        }

//        if (currentItem.bidAmount!!.toInt() > 0) {
//                holder.bid.text = "Bid"
//                holder.bid.visibility = View.VISIBLE
//        } else {
//            holder.bid.visibility = View.GONE
//        }

        holder.bid.setOnClickListener {
            onJobPropertyClick.onJobPropertyClick(currentItem)
        }

        holder.name.setOnClickListener {
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
        val bid : Button = itemView.findViewById(R.id.bidding)
    }
}