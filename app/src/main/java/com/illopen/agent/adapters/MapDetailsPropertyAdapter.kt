package com.illopen.agent.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.databinding.MapPropertyDetailsSingleItemBinding
import com.illopen.agent.model.PropertyMapAllList

class MapDetailsPropertyAdapter(var context: Context, var list: List<PropertyMapAllList>) :
    RecyclerView.Adapter<MapDetailsPropertyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.map_property_details_single_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val views = holder.binding
        val currentItem = list[position]

        views.apply {
            name.text = currentItem.propertyName
            location.text = currentItem.country
            propertyType.text = currentItem.propertyTypeName
            availableFor.text = currentItem.availableForMasterName
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val binding = MapPropertyDetailsSingleItemBinding.bind(itemView)
    }
}