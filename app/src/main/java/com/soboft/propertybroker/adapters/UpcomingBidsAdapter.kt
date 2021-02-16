package com.soboft.propertybroker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.soboft.propertybroker.R
import com.soboft.propertybroker.model.AssignedJobList
import com.soboft.propertybroker.utils.Params
import com.soboft.propertybroker.listeners.OnGoingClick

class UpcomingBidsAdapter(
    var from: String,
    var list: List<AssignedJobList>,
    var onPropertyClick: OnGoingClick
) :
    RecyclerView.Adapter<UpcomingBidsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val propertyName: TextView = itemView.findViewById(R.id.name)
        val assignTo: TextView = itemView.findViewById(R.id.assignName)
        val assignPhone: TextView = itemView.findViewById(R.id.assignPhone)
        val time : TextView = itemView.findViewById(R.id.time)
        val date : TextView = itemView.findViewById(R.id.date)
        val jobStatus: TextView = itemView.findViewById(R.id.jobStatus)
        val phone : TextView = itemView.findViewById(R.id.phone)
        val rootLayout: CardView = itemView.findViewById(R.id.rootLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.home_property_bid_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        if (from == Params.JOB_ASSIGN_TO_ME) {
            holder.assignTo.visibility = View.GONE
            holder.assignPhone.visibility = View.GONE
        } else {
            holder.assignTo.visibility = View.VISIBLE
            holder.assignPhone.visibility = View.VISIBLE
            holder.assignTo.text = currentItem.assignedUserName
            holder.assignPhone.text = currentItem.assignedPhoneNumber
        }
        holder.propertyName.text = currentItem.userName
        holder.jobStatus.text = currentItem.statusName
        holder.date.text = currentItem.jobVisitingDate
        holder.time.text = currentItem.jobVisitingTime
        holder.phone.text = currentItem.agentCodePhone

        holder.rootLayout.setOnClickListener {
            onPropertyClick.onGoingClick(currentItem)
        }
//        if (currentItem.jobStatus == 1) {
//            holder.jobStatus.text = "Job status: Started"
//        } else {
//            holder.jobStatus.text = "Job status: Yet To Start"
//        }
    }
}