package com.soboft.propertybroker.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soboft.propertybroker.R
import com.soboft.propertybroker.ui.activities.PropertyMoreDetails

class MyPropertiesAdapter(val context: Context) :
    RecyclerView.Adapter<MyPropertiesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val moreDetails: Button = itemView.findViewById(R.id.moreDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_my_property, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val currentItem = list[position]
//        holder.message.text = currentItem.message
        holder.moreDetails.setOnClickListener {
            context.startActivity(Intent(context, PropertyMoreDetails::class.java))
        }
    }

}