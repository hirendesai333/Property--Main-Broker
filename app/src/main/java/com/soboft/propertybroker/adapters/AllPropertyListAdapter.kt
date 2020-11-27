package com.soboft.propertybroker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soboft.propertybroker.R
import com.soboft.propertybroker.model.PropertyListModel
import com.soboft.properybroker.listeners.OnPropertyClick

class AllPropertyListAdapter(var list: ArrayList<PropertyListModel>, var onPropertyClick: OnPropertyClick) :
    RecyclerView.Adapter<AllPropertyListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val propertyName: TextView = itemView.findViewById(R.id.name)
        val rootLayout: RelativeLayout = itemView.findViewById(R.id.rootLayout)
        val propertyBanner: ImageView = itemView.findViewById(R.id.propertyBanner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.all_property_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.propertyName.text = currentItem.name
        holder.rootLayout.setOnClickListener {
            onPropertyClick.onPropertyClick()
        }
    }

}