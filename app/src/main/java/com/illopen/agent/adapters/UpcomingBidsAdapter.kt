package com.illopen.agent.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.model.AssignedJobList
import com.illopen.agent.listeners.OnGoingClick

class UpcomingBidsAdapter(var from: String, var list: List<AssignedJobList>, var onPropertyClick: OnGoingClick) :
    RecyclerView.Adapter<UpcomingBidsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val jobNo : TextView = itemView.findViewById(R.id.jobNo)
        val propertyName: TextView = itemView.findViewById(R.id.name)
        val time : TextView = itemView.findViewById(R.id.time)
        val date : TextView = itemView.findViewById(R.id.date)
        val created : TextView = itemView.findViewById(R.id.postDate)
        val postedBy : TextView = itemView.findViewById(R.id.assignTo)
        val totalProperty : TextView = itemView.findViewById(R.id.totalProperty)
        val jobStatus: TextView = itemView.findViewById(R.id.jobStatus)
        val phone : TextView = itemView.findViewById(R.id.assignPhone)
        val rootLayout: CardView = itemView.findViewById(R.id.rootLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_property_bid_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]

        holder.jobNo.text = "Job No: " + currentItem.jobNo.toString()
        holder.propertyName.text = currentItem.userName.toString()
        holder.jobStatus.text = currentItem.statusName
        holder.date.text = currentItem.jobVisitingDate
        holder.created.text = "Posted on: " + currentItem.createdDateStr
        holder.time.text = currentItem.jobVisitingTime
        holder.totalProperty.text = "Total Property: " + currentItem.totalProperty
        holder.postedBy.text = "Posted By: " + currentItem.userName.toString()
        holder.phone.text = currentItem.userPhoneNumber

        holder.rootLayout.setOnClickListener {
            onPropertyClick.onGoingClick(currentItem)
        }
    }
}