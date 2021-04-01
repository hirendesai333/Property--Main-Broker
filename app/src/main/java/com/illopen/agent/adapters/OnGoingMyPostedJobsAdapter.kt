package com.illopen.agent.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.model.JobPropertyList
import com.illopen.agent.ui.activities.PropertyDetail

class OnGoingMyPostedJobsAdapter (var context: Context, var list : List<JobPropertyList>)
: RecyclerView.Adapter<OnGoingMyPostedJobsAdapter.ViewHolder>() {

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val name : TextView = itemView.findViewById(R.id.name)
        val availableFor : TextView = itemView.findViewById(R.id.availableFor)
        val price : TextView = itemView.findViewById(R.id.price)
        val propertyType : TextView = itemView.findViewById(R.id.propertyType)
        val address : TextView = itemView.findViewById(R.id.address)
        val rootLayout : CardView = itemView.findViewById(R.id.rootLayout)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ongoing_my_posted_jobs_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]

        holder.name.text = currentItem.propertyName
        holder.availableFor.text = "Available for: " + currentItem.availableForMasterName
        holder.price.text = "Price: $" + currentItem.propertyPrice
        holder.address.text = "Address: " + currentItem.propertyAddress
        holder.propertyType.text = "Property type: " + currentItem.propertyTypeName

        holder.rootLayout.setOnClickListener {
            val intent =  Intent(context, PropertyDetail::class.java)
            intent.putExtra("propertyMasterId",currentItem.propertyMasterId.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}