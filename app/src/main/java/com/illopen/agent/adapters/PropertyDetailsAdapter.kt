package com.illopen.agent.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.databinding.SinglePropertyLayoutItemBinding

//class PropertyDetailsAdapter(var context: Context, var list: List<PropertyDetailsList>) :
//    RecyclerView.Adapter<PropertyDetailsAdapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): ViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.single_property_layout_item, parent, false)
//        return ViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//        val views = holder.binding
//        val currentItem = list[position]
//
//        views.name.text = currentItem.propertyDetailMasterName.toString()
//        views.visitDate.text = "Date: " + currentItem.createdDate
//        views.price.text = "Price: " + currentItem.value
//    }
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//    class ViewHolder(itemView : View)  : RecyclerView.ViewHolder(itemView) {
//        val binding = SinglePropertyLayoutItemBinding.bind(itemView)
//    }
//}