package com.illopen.agent.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.databinding.AmenitiesSingleItemBinding
import com.illopen.agent.model.PropertyMoreDetailsList

class PropertyDetailsAmenitiesAdapter(var context: Context, var list: List<PropertyMoreDetailsList>)
    : RecyclerView.Adapter<PropertyDetailsAmenitiesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.amenities_single_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val currentItem = list[position]
            property.text =   currentItem.propertyDetailMasterName + " :"
            propertyValue.text = currentItem.value
        }
    }

    override fun getItemCount(): Int {
       return  list.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val binding = AmenitiesSingleItemBinding.bind(itemView)
    }
}