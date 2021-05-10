package com.illopen.agent.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.databinding.MapPropertyDetailsSingleItemBinding
import com.illopen.agent.model.AllJobLanguageList

class MapDetailsPropertyAdapter(var context: Context, var list: ArrayList<AllJobLanguageList>) :
    RecyclerView.Adapter<MapDetailsPropertyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.map_property_details_single_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val views = holder.binding
        val currentItem = list[position]

        views.apply {
            name.text = currentItem.name
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val binding = MapPropertyDetailsSingleItemBinding.bind(itemView)
    }
}