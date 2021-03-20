package com.illopen.agent.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.databinding.AgentBidListItemBinding
import com.illopen.agent.databinding.CityListBinding
import com.illopen.agent.model.JobPropertyList

class AgentBidPropertyListAdapter(var context: Context, var list: List<JobPropertyList>) :
    RecyclerView.Adapter<AgentBidPropertyListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.agent_bid_list_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.apply {

            val currentItem = list[position]
            name.text = currentItem.propertyName
            address.text = "Address: " + currentItem.propertyAddress
            price.text = "Bid Amount: $" + currentItem.bidAmount
            propertyType.text = "propertyType: " + currentItem.propertyTypeName
            availableFor.text = "AvailableFor: " + currentItem.availableForMasterName
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val binding = AgentBidListItemBinding.bind(itemView)
    }
}