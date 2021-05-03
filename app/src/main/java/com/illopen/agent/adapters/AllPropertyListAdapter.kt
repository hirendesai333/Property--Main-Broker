package com.illopen.agent.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.model.AvailableJobs

class AllPropertyListAdapter(
    var from: String,
    var list: List<AvailableJobs>, var onPropertyClick: OnNewJobsClick)
    : RecyclerView.Adapter<AllPropertyListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val jobNo : TextView = itemView.findViewById(R.id.jobNo)
        val propertyName: TextView = itemView.findViewById(R.id.name)
        val postDate: TextView = itemView.findViewById(R.id.postDate)
        val date : TextView = itemView.findViewById(R.id.date)
        val time : TextView = itemView.findViewById(R.id.time)
        val rootLayout: CardView = itemView.findViewById(R.id.rootLayout)
        val isBidAdded: TextView = itemView.findViewById(R.id.isBidAdded)
        val totalProperty: TextView = itemView.findViewById(R.id.totalProperty)
        val bidStatusLayout: RelativeLayout = itemView.findViewById(R.id.bidStatusLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_property_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]

        holder.jobNo.text = "Job No: " + currentItem.jobNo.toString()
        holder.propertyName.text = currentItem.customerName
        holder.date.text = currentItem.jobVisitingDate
        holder.time.text = currentItem.jobVisitingTime
        holder.isBidAdded.text = currentItem.statusName
        holder.postDate.text = "Posted on: ${currentItem.createdDateStr}"
        holder.totalProperty.text = "Total Property: ${currentItem.totalProperty}"
        holder.rootLayout.setOnClickListener {
            onPropertyClick.onNewJobsClick(position,list[position])
        }
    }

    interface OnNewJobsClick {
        fun onNewJobsClick(position: Int,currentItem: AvailableJobs)
    }

}