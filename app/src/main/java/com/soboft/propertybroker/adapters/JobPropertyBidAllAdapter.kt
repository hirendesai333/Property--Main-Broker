package com.soboft.propertybroker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soboft.propertybroker.R
import com.soboft.propertybroker.databinding.JobPropertyAllBidListBinding
import com.soboft.propertybroker.model.JobPropertyBidAllList
import org.w3c.dom.Text

class JobPropertyBidAllAdapter(var context: Context,var list: List<JobPropertyBidAllList>)
    : RecyclerView.Adapter<JobPropertyBidAllAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.job_property_all_bid_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val currentItem = list[position]

        holder.name.text = currentItem.amount.toString()
        holder.propertyType.text = currentItem.note.toString()
        holder.address.text = currentItem.propertyAddress.toString()
    }

    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val name : TextView = itemView.findViewById(R.id.name)
        val propertyType : TextView = itemView.findViewById(R.id.propertyType)
        val address : TextView = itemView.findViewById(R.id.add)
    }
}