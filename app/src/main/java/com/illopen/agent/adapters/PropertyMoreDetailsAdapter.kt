package com.illopen.agent.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.databinding.SinglePropertyMoreDetailsBinding
import com.illopen.agent.model.PropertyMoreDetailsList

class PropertyMoreDetailsAdapter(var context: Context, var list: List<PropertyMoreDetailsList>)
    : RecyclerView.Adapter<PropertyMoreDetailsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.single_property_more_details, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val views = holder.binding
        val currentItem = list[position]

        views.propertyDetailName.text = currentItem.name.toString()
//        views.price.text = currentItem.price
//        views.date.text = currentItem.createddate
    }

    override fun getItemCount(): Int {
      return list.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val binding = SinglePropertyMoreDetailsBinding.bind(itemView)
    }

}